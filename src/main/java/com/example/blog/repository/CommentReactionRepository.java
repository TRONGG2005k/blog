package com.example.blog.repository;

import com.example.blog.entity.Comment;
import com.example.blog.entity.CommentReaction;
import com.example.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentReactionRepository extends JpaRepository<CommentReaction, String> {
    Optional<CommentReaction> findByUserAndComment(User user, Comment comment);
}
