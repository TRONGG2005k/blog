package com.example.blog.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
public class CommentReactionResponse {
    private String type;
    private String commentId;
    private UserResponse user;
}