package com.example.blog.mapper;

import com.example.blog.dto.response.PostReactionResponse;
import com.example.blog.entity.PostReaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {UserMapper.class})
public interface PostReactionMapper {
    PostReactionMapper INSTANCE = Mappers.getMapper(PostReactionMapper.class);

    @Mapping(target = "postId", source = "post.id")
    PostReactionResponse toPostReactionResponse(PostReaction postReaction);
}
