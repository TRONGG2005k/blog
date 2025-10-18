package com.example.blog.dto.request;

import com.example.blog.Enum.ReactionType;
import com.example.blog.validation.EnumValidator;
import lombok.*;

@Getter
@Setter
public class CommentReactionRequest {
    private String commentId;
    @EnumValidator(enumClass = ReactionType.class, message = "Invalid reaction type")
    private String type; // ví dụ: "LIKE", "LOVE", "HAHA"
}