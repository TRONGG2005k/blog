package com.example.blog.repository;

import com.example.blog.entity.User;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("select u from User u where u.isDeleted = false")
    Page<User> findAllUser(Pageable pageable);
    Boolean existsByUsername(String name);
    Boolean existsByEmail(String e);
    Optional<User> findByUsername(String name);
}
