package com.example.blog.repository;

import com.example.blog.entity.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaRepository extends JpaRepository<Media, String> {
    Optional<Media> findByUrl(String url);
    List<Media> findAllByUrlIn(List<String> urls);
}
