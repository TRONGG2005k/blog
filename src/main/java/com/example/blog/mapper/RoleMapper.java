package com.example.blog.mapper;

import com.example.blog.dto.response.RoleResponse;
import com.example.blog.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleResponse roleToRoleResponse(Role role);
}
