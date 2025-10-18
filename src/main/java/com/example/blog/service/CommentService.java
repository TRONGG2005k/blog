package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.dto.request.CommentRequest;
import com.example.blog.dto.response.CommentResponse;
import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // ✅ CREATE
    public CommentResponse create(CommentRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        Comment parentComment = null;
        if (request.getParentCommentId() != null && !request.getParentCommentId().isBlank()) {
            parentComment = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));
        }

        Comment comment = Comment.builder()
                .user(user)
                .post(post)
                .content(request.getContent())
                .parent(parentComment)
                .build();

        Comment saved = commentRepository.save(comment);

        return toResponse(saved);
    }

    // ✅ READ - Get all comments by postId
    public List<CommentResponse> getCommentsByPostId(String postId) {
        List<Comment> comments = commentRepository.findByPostIdAndParentIsNullAndIsDeletedFalse(postId);

        // Map thành response + lấy replies đệ quy
        return comments.stream()
                .map(this::toResponseWithReplies)
                .collect(Collectors.toList());
    }

    // ✅ UPDATE
    public CommentResponse update(String commentId, CommentRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        comment.setContent(request.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updated = commentRepository.save(comment);

        return toResponse(updated);
    }

    // ✅ DELETE (Soft delete)
    public void delete(String commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        comment.setIsDeleted(true);
        comment.setDeletedAt(LocalDateTime.now());
        commentRepository.save(comment);
    }

    // ---------------------------- Helper ----------------------------

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .username(comment.getUser().getUsername())
                .userId(comment.getUser().getId())
                .postId(comment.getPost().getId())
                .parentCommentId(comment.getParent() != null ? comment.getParent().getId() : null)
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .reactionCount(comment.getReactions() != null ? comment.getReactions().size() : 0)
                .build();
    }

    private CommentResponse toResponseWithReplies(Comment comment) {
        CommentResponse response = toResponse(comment);

        List<CommentResponse> replies = comment.getReplies() == null ? List.of() :
                comment.getReplies().stream()
                        .filter(reply -> !Boolean.TRUE.equals(reply.getIsDeleted()))
                        .map(this::toResponseWithReplies)
                        .collect(Collectors.toList());

        response.setReplies(replies);
        return response;
    }
}
