package com.example.blog.exceptionHanding.handling;

import com.example.blog.exceptionHanding.exception.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;


@RestControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<?> handleAppException(AppException ex) {
        var code = ex.getErrorCode();

        var body = Map.of(
                "timestamp", LocalDateTime.now(),
                "code", code.getCode(),
                "message", code.getMessage()
        );

        return ResponseEntity.status(code.getCode()).body(body);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        // Lấy lỗi đầu tiên trong danh sách lỗi
        var firstError = ex.getBindingResult().getFieldErrors().getFirst();

        String message = firstError.getDefaultMessage();
        var body = Map.of(
                "timestamp", LocalDateTime.now(),
                "code", 400,
                "message", Objects.requireNonNull(message)
        );
        // Trả về JSON đơn giản
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
        // Nếu muốn bao gồm cả tên field thì thay dòng trên bằng:
        // .body(Map.of(field, message));
    }
}
