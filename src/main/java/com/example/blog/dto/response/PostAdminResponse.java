package com.example.blog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostAdminResponse {
    String id;
    String caption;
    String username;
    List<String> mediaUrls;
    Long reactionCount;
    Long countComment;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
