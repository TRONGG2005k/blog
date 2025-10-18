package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.dto.request.PostReactionRequest;
import com.example.blog.dto.response.PostReactionResponse;
import com.example.blog.entity.Post;
import com.example.blog.entity.PostReaction;
import com.example.blog.entity.User;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.mapper.UserMapper;
import com.example.blog.repository.PostReactRepository;
import com.example.blog.repository.PostRepository;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostReactionService {

    private final PostReactRepository postReactRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostReactionResponse create(PostReactionRequest request) {

        if (request.getType() == null) {
            throw new AppException(ErrorCode.INVALID_REACTION_TYPE);
        }

        Post post = postRepository.findById(request.getPostId())
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        PostReaction existing = postReactRepository.findByUserAndPost(user, post).orElse(null);

        if (existing != null) {

            if (existing.getType().equals(request.getType())) {
                postReactRepository.delete(existing);
                return buildResponse(null, post, user);
            }

            existing.setType(request.getType());
            postReactRepository.save(existing);
            return buildResponse(existing.getType(), post, user);
        }


        PostReaction postReaction = postReactRepository.save(
                PostReaction.builder()
                        .post(post)
                        .user(user)
                        .type(request.getType())
                        .build()
        );

        return buildResponse(postReaction.getType(), post, user);
    }

    private PostReactionResponse buildResponse(String type, Post post, User user) {
        return PostReactionResponse.builder()
                .type(type)
                .postId(post.getId())
                .user(UserMapper.INSTANCE.userToUserResponse(user))
                .build();
    }
}
