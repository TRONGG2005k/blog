package com.example.blog.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class Oauth2UserResponse {
    private String sub;      // ID duy nhất của user trong hệ thống Google
    private String name;
    private String given_name;
    private String family_name;
    private String picture;
    private String email;
    private Boolean email_verified;
    private String locale;
}