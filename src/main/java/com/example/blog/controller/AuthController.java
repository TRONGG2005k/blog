package com.example.blog.controller;

import com.example.blog.dto.request.CreateUserRequest;
import com.example.blog.dto.request.LoginRequest;
import com.example.blog.dto.response.LoginResponse;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.service.AuthService;
import com.example.blog.service.JwtService;
import com.example.blog.service.UserService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) throws Exception {
        var loginResponse = authService.login(request);

        var cookie = jwtService.createRefreshCookie(loginResponse.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LoginResponse.builder()
                        .token(loginResponse.getToken())
                        .role(loginResponse.getRole())
                        .build());
    }

    @PostMapping("/register")
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest request){
        System.out.println("✅ Controller reached!");
        return ResponseEntity.ok().body(userService.create(request));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    ) throws ParseException, JOSEException {
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body("Refresh token not found.");
        }
        authService.logout(refreshToken);
        // Xoá cookie trên trình duyệt
        ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false) // nếu bạn dùng HTTPS
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body("Logout success");
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken
    ) throws ParseException, JOSEException {
        try {
            return ResponseEntity.ok(authService.refreshToken(refreshToken));
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(e.getErrorCode().getMessage());
        }
    }
    @PostMapping("/outbound/authentication")
    public ResponseEntity<LoginResponse> oauth2Login(
            @RequestParam("code") String code) throws Exception {
        var loginResponse = authService.loginByGg(code);

        var cookie = jwtService.createRefreshCookie(loginResponse.getRefreshToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(LoginResponse.builder()
                        .token(loginResponse.getToken())
                        .role(loginResponse.getRole())
                        .build());
    }
}
