package com.example.blog.repository;

import com.example.blog.redis.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByJwtID(String jwtID);
    void  deleteByJwtID(String jwtID);
}
