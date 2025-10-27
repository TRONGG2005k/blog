package com.example.blog.service;

import com.example.blog.dto.response.RoleResponse;
import com.example.blog.mapper.RoleMapper;
import com.example.blog.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public List<RoleResponse> getRole(){
        return roleRepository.findAll().stream().map(
                RoleMapper.INSTANCE::roleToRoleResponse
        ).toList();
    }
}
