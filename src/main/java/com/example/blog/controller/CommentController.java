package com.example.blog.controller;

import com.example.blog.dto.request.CommentRequest;
import com.example.blog.dto.response.CommentResponse;
import com.example.blog.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // ✅ CREATE
    @PostMapping
    public ResponseEntity<CommentResponse> createComment(@Valid @RequestBody CommentRequest request) {
        CommentResponse response = commentService.create(request);
        return ResponseEntity.ok(response);
    }

    // ✅ READ (Lấy tất cả comment của 1 bài post)
    @GetMapping("/{postId}/comments")
    public ResponseEntity<?> getComments(
            @PathVariable String postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(commentService.getCommentsByPostId(postId, page, size));
    }


    // ✅ UPDATE
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable String commentId,
            @Valid @RequestBody CommentRequest request) {

        CommentResponse response = commentService.update(commentId, request);
        return ResponseEntity.ok(response);
    }

    // ✅ DELETE (Soft delete)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable String commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}
