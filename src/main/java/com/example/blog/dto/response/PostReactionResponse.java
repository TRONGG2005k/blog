package com.example.blog.dto.response;


import lombok.*;
import lombok.experimental.FieldDefaults;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostReactionResponse {

    String type;

    String postId;

    UserResponse user;

}
