package com.example.blog.controller;

import com.example.blog.dto.request.CreateUserRequest;
import com.example.blog.dto.request.UpdateUserRequest;
import com.example.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok().body(userService.findAll());
    }

    @GetMapping("/info")
    public ResponseEntity<?> info(){
        return ResponseEntity.ok().body(userService.info());
    }
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody CreateUserRequest request){
        System.out.println("✅ Controller reached!");
        return ResponseEntity.ok().body(userService.create(request));
    }

    @PutMapping("/{id}")
    public  ResponseEntity<?> update(
            @Valid @RequestBody UpdateUserRequest request,
            @PathVariable("id") String id
    ){
        System.out.println("✅ Controller reached!");
        return ResponseEntity.ok().body(userService.update(request, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  ResponseEntity<?> delete(
            @PathVariable("id") String id
    ){
        System.out.println("✅ Controller reached!");
        return ResponseEntity.ok().body(userService.delete(id));
    }
}
