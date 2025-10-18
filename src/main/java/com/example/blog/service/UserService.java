package com.example.blog.service;

import com.example.blog.Enum.ErrorCode;
import com.example.blog.dto.request.CreateUserRequest;
import com.example.blog.dto.request.UpdateUserRequest;
import com.example.blog.dto.response.UserResponse;
import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.exceptionHanding.exception.AppException;
import com.example.blog.mapper.UserMapper;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper mapper;

    public List<UserResponse> findAll(){
        return userRepository.findAllUser().stream().map(
                mapper::userToUserResponse
        ).toList();
    }

    public UserResponse create(CreateUserRequest request){
        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_ALREADY_EXIST);
        }
        if(userRepository.existsByEmail(request.getEmail())){
            throw new AppException(ErrorCode.EMAIL_ALREADY_EXIST);
        }
        User user = mapper.createUserRequestToUser(request);
        user.setActive(true);
        Role role = roleRepository.findByName("ROLE_USER").orElseGet(
                () -> roleRepository.save(
                        Role.builder().name("ROLE_USER").build()
                )
        );
        user.getRoles().add(role);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return mapper.userToUserResponse(user);
    }

    public UserResponse update(UpdateUserRequest request, String id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        mapper.updateUserRequestToUser(request, user);
        user.setUpdatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return mapper.userToUserResponse(user);
    }

    public String delete(String id){
        User user = userRepository.findById(id).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        user.setDeletedAt(LocalDateTime.now());
        user.setIsDeleted(true);
        userRepository.save(user);
        return "delete success";
    }

    public UserResponse info(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_FOUND)
        );
        return mapper.userToUserResponse(user);
    }

}
