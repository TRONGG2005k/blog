package com.example.blog.configuration;

import com.example.blog.entity.Role;
import com.example.blog.entity.User;
import com.example.blog.repository.RoleRepository;
import com.example.blog.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class StartupTask {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    
    @PostConstruct
    public void init(){
        Role role = roleRepository.findByName("ROLE_ADMIN").orElseGet(
                ()->roleRepository.save(
                        Role.builder()
                                .name("ROLE_ADMIN")
                                .build()
                )
        );

        Set<Role> roles = new HashSet<>();
        roles.add(role);

        userRepository.findByUsername("admin").orElseGet(
                () -> userRepository.save(
                        User.builder()
                                .username("admin")
                                .password(passwordEncoder.encode("admin"))
                                .email("admin@example.com")
                                .roles(roles)
                                .active(true)
                                .build()
                )
        );
    }
}
