package com.example.blog.controller;

import com.example.blog.dto.request.CommentReactionRequest;
import com.example.blog.dto.response.CommentReactionResponse;
import com.example.blog.service.CommentReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comment-reactions")
@RequiredArgsConstructor
public class CommentReactionController {

    private final CommentReactionService commentReactionService;

    @PostMapping
    public ResponseEntity<CommentReactionResponse> reactToComment(
            @Valid  @RequestBody CommentReactionRequest request) {

        CommentReactionResponse response = commentReactionService.create(request);
        return ResponseEntity.ok(response);
    }
}
