package com.example.blog.dto.response;


import com.example.blog.entity.Media;
import com.example.blog.entity.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostResponse {
    private String id;
    private String caption;
    private UserResponse user;
    private List<CommentResponse> comments;
    private List<String> mediaUrls;
    private long reactions;
    private Set<Tag> tags;
    private LocalDateTime createdAt;
}
