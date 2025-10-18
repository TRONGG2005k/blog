package com.example.blog.mapper;

import com.example.blog.dto.response.PostResponse;
import com.example.blog.entity.Media;
import com.example.blog.entity.Post;
import com.example.blog.entity.Tag;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(uses = {UserMapper.class, PostReactionMapper.class, CommentMapper.class})
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "caption", source = "caption")
    @Mapping(target = "user", source = "user")
    @Mapping(target = "comments", source = "comments")
    @Mapping(target = "mediaList", source = "mediaList")
    @Mapping(target = "reactions", source = "reactions")
    @Mapping(target = "tags", source = "tags")
    PostResponse toPostResponse(Post post);

    List<PostResponse> toPostResponseList(List<Post> posts);

    // Helper methods
    default List<String> mapMediaUrls(Post post) {
        if (post.getMediaList() == null) return Collections.emptyList();
        return post.getMediaList().stream().map(Media::getUrl).collect(Collectors.toList());
    }

    default List<String> mapTagNames(Post post) {
        if (post.getTags() == null) return Collections.emptyList();
        return post.getTags().stream().map(Tag::getName).collect(Collectors.toList());
    }
}
