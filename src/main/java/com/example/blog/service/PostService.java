package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.dto.request.PostRequest;
import com.example.blog.dto.response.PostResponse;
import com.example.blog.dto.response.PostSummaryResponse;
import com.example.blog.entity.Media;
import com.example.blog.entity.Post;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.mapper.PostMapper;
import com.example.blog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PostService {

    private final TagRepository tagRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final MediaRepository mediaRepository;
    private final PostReactRepository postReactRepository;
    private final CommentRepository commentRepository;
    public Page<PostSummaryResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Post> posts = postRepository.findAllByIsDeletedFalse(pageable);

        List<String> postIds = posts.stream()
                .map(Post::getId)
                .toList();

        List<Object[]> reactionCounts = postReactRepository.countReactionsForPosts(postIds);
        Map<String, Long> reactionCountMap = reactionCounts.stream()
                .collect(Collectors.toMap(
                        row -> (String) row[0],
                        row -> ((Number) row[1]).longValue()
                ));

        return posts.map(post -> {
            List<String> mediaUrls = post.getMediaList()
                    .stream()
                    .map(Media::getUrl)
                    .toList();
            long reactionCount = reactionCountMap.getOrDefault(post.getId(), 0L);
            return PostSummaryResponse.builder()
                    .id(post.getId())
                    .caption(post.getCaption())
                    .username(post.getUser().getUsername())
                    .mediaUrls(mediaUrls)
                    .reactionCount(reactionCount)
                    .createdAt(post.getCreatedAt())
                    .countComment(commentRepository.countByPost(post))
                    .build();
        });
    }



    public PostResponse getPostById(String id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        return PostMapper.INSTANCE.toPostResponse(post);
    }

    public PostResponse createPostWithMedia(PostRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Media> medias = Collections.emptyList();
        if (request.getMediaUrl() != null && !request.getMediaUrl().isEmpty()) {
            medias = mediaRepository.findAllByUrlIn(request.getMediaUrl());
        }

        var tags = tagRepository.findByNameIn(request.getTagName());

        Post post = Post.builder()
                .caption(request.getCaption())
                .user(user)
                .tags(tags)
                .mediaList(medias)
                .build();

        for (var media : medias) {
            media.setPost(post);
        }

        postRepository.save(post); // Cascade tự lưu media

        return PostMapper.INSTANCE.toPostResponse(post);
    }

    public PostResponse updatePost(String postId, PostRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Tìm post và kiểm tra quyền
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED); // Không phải chủ bài viết
        }

        // Cập nhật caption
        post.setCaption(request.getCaption());

        // Cập nhật tags
        var tags = tagRepository.findByNameIn(request.getTagName());
        post.setTags(tags);

        // Cập nhật media
        List<Media> newMedias = Collections.emptyList();
        if (request.getMediaUrl() != null && !request.getMediaUrl().isEmpty()) {
            newMedias = mediaRepository.findAllByUrlIn(request.getMediaUrl());
        }

        // Xoá liên kết media cũ
        post.getMediaList().clear();

        // Gán media mới
        for (var media : newMedias) {
            media.setPost(post);
        }
        post.getMediaList().addAll(newMedias);

        postRepository.save(post);

        return PostMapper.INSTANCE.toPostResponse(post);
    }

    public void deletePost(String postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        var post = postRepository.findById(postId)
                .orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        // ✅ Xoá mềm
        post.setIsDeleted(true);
        post.setDeletedAt(LocalDateTime.now());
        postRepository.save(post);
    }


}
