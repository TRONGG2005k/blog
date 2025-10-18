package com.example.blog.exceptionHanding.exception;

import com.example.blog.Enum.ErrorCode;
import lombok.*;


@Getter
@Setter
@Builder
public class AppException extends RuntimeException{

    private final ErrorCode errorCode;

    public AppException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
