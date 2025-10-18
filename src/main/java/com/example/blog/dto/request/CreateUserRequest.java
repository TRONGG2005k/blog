package com.example.blog.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateUserRequest {
    @NotNull(message = "Name must be not null")
    @NotBlank(message = "Name is mandatory")
    @Size(min = 3, max = 100, message = "User name must be greater than 3 characters ")
    String username;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 100, message = "Password must be greater than 8 characters ")
    @NotNull(message = "Password must be not null")
    String password;

    @NotBlank(message = "Email is mandatory")
    @NotNull(message = "Email must be not null")
    String email;
}
