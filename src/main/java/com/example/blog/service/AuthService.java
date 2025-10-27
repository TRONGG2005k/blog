package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.client.AuthenticateGgService;
import com.example.blog.dto.request.LoginRequest;
import com.example.blog.dto.response.ExchangeGoogleTokenResponse;
import com.example.blog.dto.response.LoginResponse;
import com.example.blog.dto.response.Oauth2UserResponse;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.redis.RefreshToken;
import com.example.blog.repository.RefreshTokenRepository;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticateGgService authenticateGgService;
    private final RoleRepository roleRepository;


    public LoginResponse login(LoginRequest request) throws JOSEException, ParseException {
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.INVALID_USERNAME_OR_PASSWORD);
        }
        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        var refreshTokenClaims = jwtService.getClaims(refreshToken);

        refreshTokenRepository.save(RefreshToken.builder()
                .jwtID(refreshTokenClaims.getJWTClaimsSet().getJWTID())
                .username(user.getUsername())
                .ttl(2_592_000L)
                .build());


        log.warn(jwtService.getClaims(accessToken).getJWTClaimsSet().getExpirationTime().toString());
        String role = refreshTokenClaims.getJWTClaimsSet().getClaimAsString("scope");
        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .role(role)
                .build();
    }

    public void logout(String refreshToken) throws ParseException {
        var claims = jwtService.getClaims(refreshToken).getJWTClaimsSet();
        String jwtId = claims.getJWTID();
        String username = claims.getSubject();

        userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        refreshTokenRepository.findByJwtID(jwtId).orElseThrow(
                () -> new AppException(ErrorCode.INVALID_TOKEN)
        );

        refreshTokenRepository.deleteById(username);
    }

    public LoginResponse refreshToken(String refreshToken) throws JOSEException, ParseException {
        jwtService.verifyRefreshToken(refreshToken);
        String username = jwtService.getClaims(refreshToken).getJWTClaimsSet().getSubject();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        String accessToken = jwtService.generateAccessToken(user);

        return LoginResponse.builder()
                .token(accessToken)
                .build();
    }

    public LoginResponse loginByGg(String code) throws JOSEException, ParseException {
        ExchangeGoogleTokenResponse response = authenticateGgService.exchangeGoogleToken(code);

        Oauth2UserResponse userResponse = authenticateGgService.getUserInfo("json", response.getAccessToken());

        User user = userRepository.findByUsername(userResponse.getEmail()).orElseGet(
                () -> User.builder()
                        .active(true)
                        .username(userResponse.getEmail())
                        .email(userResponse.getEmail())
                        .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                        .build()
        );
        Role role = roleRepository.findByName("ROLE_USER").orElseGet(
                () -> roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                )
        );
        user.getRoles().add(role);

        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);

        String refreshToken = jwtService.generateRefreshToken(user);

        var refreshTokenClaims = jwtService.getClaims(refreshToken);

        refreshTokenRepository.save(RefreshToken.builder()
                .jwtID(refreshTokenClaims.getJWTClaimsSet().getJWTID())
                .username(user.getUsername())
                .ttl(2_592_000L)
                .build());
        String roles = refreshTokenClaims.getJWTClaimsSet().getClaimAsString("scope");
        return LoginResponse.builder()
                .token(accessToken)
                .refreshToken(refreshToken)
                .role(roles)
                .build();

    }
}
