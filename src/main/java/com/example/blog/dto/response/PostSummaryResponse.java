package com.example.blog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostSummaryResponse {
    String id;
    String caption;
    String username;
    List<String> mediaUrls;
    Long reactionCount;
    Long countComment;
    LocalDateTime createdAt;
}
