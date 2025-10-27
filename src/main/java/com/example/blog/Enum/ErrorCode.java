package com.example.blog.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(404, "User not found"),
    COMMENT_NOT_FOUND(404, "Comment not found"),
    INVALID_TOKEN(401, "Invalid token"),
    ROLE_NOT_FOUND(404, "Role not found"),
    TOKEN_HAS_EXPIRED(401, "token has expired"),
    EMAIL_ALREADY_EXIST(409, "Email already exist"),
    INVALID_REACTION_TYPE(401, "Invalid reaction type"),
    INVALID_TOKEN_TYPE(401, "invalid token type"),
    INVALID_USERNAME_OR_PASSWORD(401, "Invalid username or password"),
    FILE_NOT_FOUND(404, "Media not found"),
    POST_NOT_FOUND(404, "post not found"),
    UNAUTHORIZED(403, "un authorized"),
    USER_ALREADY_EXIST(409, "User already exist");
    private final int code;
    private final String message;
}
