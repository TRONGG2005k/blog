package com.example.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentRequest {

    @NotBlank(message = "Nội dung bình luận không được để trống")
    String content;

    @NotNull(message = "postId không được để trống")
    String postId;// ID của bài post được comment
    String parentCommentId; // Nếu là reply thì gửi id comment cha, nếu là comment mới thì để null
}