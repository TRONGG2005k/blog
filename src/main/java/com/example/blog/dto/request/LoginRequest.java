package com.example.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    @NotNull(message = "Name must be not null")
    @NotBlank(message = "Name is mandatory")
    String username;

    @NotBlank(message = "Password is mandatory")
    @NotNull(message = "Password must be not null")
    String password;
}
