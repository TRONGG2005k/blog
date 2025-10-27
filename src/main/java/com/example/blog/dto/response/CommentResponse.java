package com.example.blog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentResponse {
    String id;
    String content;

    String username;      // tên người comment
    String userId;

    String postId;

    String parentCommentId; // id comment cha (nếu có)

    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    int reactionCount; // tổng số reaction của comment

    boolean canEdit;

    boolean canDelete;
}