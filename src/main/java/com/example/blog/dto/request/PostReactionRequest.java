package com.example.blog.dto.request;

import com.example.blog.Enum.ReactionType;
import com.example.blog.validation.EnumValidator;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostReactionRequest {
    @EnumValidator(enumClass = ReactionType.class, message = "Invalid reaction type")
    String type;

    String postId;
}
