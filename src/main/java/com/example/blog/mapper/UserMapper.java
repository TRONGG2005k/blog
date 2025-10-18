package com.example.blog.mapper;

import com.example.blog.dto.request.CreateUserRequest;
import com.example.blog.dto.request.UpdateUserRequest;
import com.example.blog.dto.response.UserResponse;
import com.example.blog.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponse userToUserResponse(User user);

    User createUserRequestToUser(CreateUserRequest user);

    void updateUserRequestToUser(UpdateUserRequest request, @MappingTarget User user);
}
