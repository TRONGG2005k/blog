package com.example.blog.dto.response;

import lombok.*;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
public class UserProfileResponse {
    private String id;
    private String username;
    private String email;
    private Boolean active;
    private Set<String> roles;
    private List<PostSummary> posts; // List nhẹ, không vòng lặp

    @Getter
    @Setter
    @Builder
    public static class PostSummary {
        private String id;
        private String caption;
        private boolean isDelete;
        private String createdAt;
    }
}

