package com.example.blog.controller;

import com.example.blog.dto.request.PostReactionRequest;
import com.example.blog.dto.response.PostReactionResponse;
import com.example.blog.service.PostReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post-reactions")
@RequiredArgsConstructor
public class PostReactionController {

    private final PostReactionService postReactionService;

    /**
     * API toggle reaction cho post.
     * - Nếu user chưa reaction → thêm mới
     * - Nếu user đã reaction cùng loại → xoá
     * - Nếu user đã reaction khác loại → cập nhật
     */
    @PostMapping
    public ResponseEntity<PostReactionResponse> reactToPost(@Valid @RequestBody PostReactionRequest request) {
        PostReactionResponse response = postReactionService.create(request);
        return ResponseEntity.ok(response);
    }
}
