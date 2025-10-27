package com.example.blog.mapper;

import com.example.blog.dto.response.CommentResponse;
import com.example.blog.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(uses = {UserMapper.class})
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "parentCommentId", source = "parent.id")
    @Mapping(target = "reactionCount", expression = "java(comment.getReactions() != null ? comment.getReactions().size() : 0)")
    CommentResponse toResponse(Comment comment);

    List<CommentResponse> toResponseList(List<Comment> comments);

    // default method mới, map thêm canEdit và canDelete
    default CommentResponse toResponse(Comment comment, String currentUsername, boolean isAdmin) {
        CommentResponse response = toResponse(comment); // map các trường bình thường
        response.setCanEdit(comment.getUser().getUsername().equals(currentUsername));
        response.setCanDelete(response.isCanEdit() || isAdmin);
        return response;
    }
}
