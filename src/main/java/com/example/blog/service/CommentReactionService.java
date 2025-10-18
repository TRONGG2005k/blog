package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.Enum.ReactionType;
import com.example.blog.dto.request.CommentReactionRequest;
import com.example.blog.dto.response.CommentReactionResponse;
import com.example.blog.entity.Comment;
import com.example.blog.entity.CommentReaction;
import com.example.blog.entity.User;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.mapper.UserMapper;
import com.example.blog.repository.CommentReactionRepository;
import com.example.blog.repository.CommentRepository;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class CommentReactionService {

    private final CommentReactionRepository commentReactionRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public CommentReactionResponse create(CommentReactionRequest request) {

        if (request.getType() == null) {
            throw new AppException(ErrorCode.INVALID_REACTION_TYPE);
        }
        boolean isValid = Arrays.stream(ReactionType.values())
                .anyMatch(type -> type.name().equalsIgnoreCase(request.getType()));

        if (!isValid) {
            throw new AppException(ErrorCode.INVALID_REACTION_TYPE);
        }

        Comment comment = commentRepository.findById(request.getCommentId())
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        CommentReaction existing = commentReactionRepository.findByUserAndComment(user, comment).orElse(null);

        // Nếu đã tồn tại reaction của user trên comment này
        if (existing != null) {

            // Nếu type trùng => bỏ reaction
            if (existing.getType().equals(request.getType())) {
                commentReactionRepository.delete(existing);
                return buildResponse(null, comment, user);
            }

            // Nếu khác => đổi type
            existing.setType(request.getType());
            commentReactionRepository.save(existing);
            return buildResponse(existing.getType(), comment, user);
        }

        // Nếu chưa có => tạo mới
        CommentReaction commentReaction = commentReactionRepository.save(
                CommentReaction.builder()
                        .comment(comment)
                        .user(user)
                        .type(request.getType())
                        .build()
        );

        return buildResponse(commentReaction.getType(), comment, user);
    }

    private CommentReactionResponse buildResponse(String type, Comment comment, User user) {
        return CommentReactionResponse.builder()
                .type(type)
                .commentId(comment.getId())
                .user(UserMapper.INSTANCE.userToUserResponse(user))
                .build();
    }
}
