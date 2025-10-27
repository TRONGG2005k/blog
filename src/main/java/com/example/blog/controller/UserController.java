package com.example.blog.controller;

import com.example.blog.dto.request.ChangeRole;
import com.example.blog.dto.request.CreateUserRequest;
import com.example.blog.dto.request.UpdateUserRequest;
import com.example.blog.dto.response.UserResponse;
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
    public ResponseEntity<?> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(userService.findAll(page, size));
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getProfile(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public  ResponseEntity<?> delete(
            @PathVariable("id") String id
    ){
        System.out.println("✅ Controller reached!");
        return ResponseEntity.ok().body(userService.delete(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/change-role")
    public ResponseEntity<UserResponse> changeUserRole(
            @PathVariable String id,
            @Valid @RequestBody ChangeRole request
    ) {
        UserResponse response = userService.changeRole(id, request);
        return ResponseEntity.ok(response);
    }
}
