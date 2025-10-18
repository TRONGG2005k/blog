package com.example.blog.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PostRequest {
    @NotBlank(message = "Caption không được để trống")
    @Size(max = 500, message = "Caption tối đa 500 ký tự")
    String caption;
    @NotEmpty(message = "Phải có ít nhất 1 tag")
    Set<String> tagName;

    List<String> mediaUrl;
}

