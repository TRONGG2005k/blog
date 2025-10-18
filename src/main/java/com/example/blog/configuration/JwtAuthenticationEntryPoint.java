package com.example.blog.configuration;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // Log lỗi nếu cần thiết
        log.error("❌ Authentication failed for path: {} | Error: {}",
                request.getRequestURI(),
                authException.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String errorMessage = "{\"success\": false, \"message\": \"Unauthenticated: Invalid or expired JWT token\"}";

        // Ghi thông điệp lỗi vào response
        response.getWriter().write(errorMessage);
        response.flushBuffer();
    }
}
