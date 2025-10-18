package com.example.blog.controller;

import com.example.blog.dto.request.PostRequest;
import com.example.blog.dto.response.PostResponse;
import com.example.blog.dto.response.PostSummaryResponse;
import com.example.blog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<Page<PostSummaryResponse>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<PostSummaryResponse> response = postService.getAllPosts(page, size);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPostById(@PathVariable String id) {
        PostResponse post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable("id") String postId,
            @Valid @RequestBody PostRequest postRequest
    ) {
        PostResponse updatedPost = postService.updatePost(postId, postRequest);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        PostResponse response = postService.createPostWithMedia(postRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable("id") String postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build(); // HTTP 204
    }
}
