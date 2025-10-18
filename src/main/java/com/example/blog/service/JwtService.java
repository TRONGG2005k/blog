package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.repository.RefreshTokenRepository;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import com.example.blog.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.signerKeyAccess}")
    protected String SECRET_KEY_ACCESS;
    @Value("${jwt.signerKeyRefresh}")
    protected String SECRET_KEY_REFRESH;
    @Value("${jwt.valid-duration}")
    protected long ACCESS_TOKEN_EXPIRATION;

    @Value("${jwt.refreshable-duration}")
    protected long REFRESH_TOKEN_EXPIRATION;

    private final RefreshTokenRepository refreshTokenRepository;

    public String generateAccessToken(User user) throws JOSEException {
        return generateToken(user, SECRET_KEY_ACCESS, "access", ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(User user) throws JOSEException {
        return generateToken(user, SECRET_KEY_REFRESH, "refresh", REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(User user, String secret, String type, long expirationMs) throws JOSEException {
        JWTClaimsSet claims = buildClaims(user, type, expirationMs);

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS512), claims);

        signedJWT.sign(new MACSigner(secret.getBytes()));

        return signedJWT.serialize();
    }

    private JWTClaimsSet buildClaims(User user, String type, long expirationMs) {
        StringJoiner scopes = new StringJoiner(" ");
        user.getRoles().forEach(role -> scopes.add(role.getName()));

        return new JWTClaimsSet.Builder()
                .issueTime(new Date())
                .subject(user.getUsername())
                .claim("scope", scopes.toString())
                .claim("type", type)
                .issuer("myapp.com")
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(
                        new Date(
                                Instant.now().plus(expirationMs, ChronoUnit.SECONDS)
                                        .toEpochMilli()
                        )
                )
                .build();
    }

    public ResponseCookie createRefreshCookie(String refreshToken) {
        return ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(false) // true nếu HTTPS
                .path("/")
                .maxAge(30 * 24 * 60 * 60) // 7 ngày
                .sameSite("Lax")
                .build();
    }

    public SignedJWT getClaims(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    public boolean verifyRefreshToken(String token) throws ParseException, JOSEException {
        SignedJWT jwt = getClaims(token);
        Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();
        String jwtId = jwt.getJWTClaimsSet().getJWTID();
        String type = jwt.getJWTClaimsSet().getStringClaim("type");
        if (!"refresh".equals(type)) {
            throw new AppException(ErrorCode.INVALID_TOKEN_TYPE);
        }
        if(!jwt.verify(new MACVerifier(SECRET_KEY_REFRESH.getBytes()))){
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
        if(expirationTime == null || expirationTime.before(new Date())){
            throw new AppException(ErrorCode.TOKEN_HAS_EXPIRED);
        }
        refreshTokenRepository.findByJwtID(jwtId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_TOKEN));
        return true;
    }
}
