package com.example.blog.repository;

import com.example.blog.entity.Post;
import com.example.blog.entity.PostReaction;
import com.example.blog.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostReactRepository extends JpaRepository<PostReaction, String> {
    Optional<PostReaction> findByUserAndPost(User user, Post post);
    @Query(value = """
        SELECT r.post.id AS postId, COUNT(r.id) AS count
        FROM PostReaction r
        WHERE r.post.id IN :postIds
        GROUP BY r.post.id
    """)
    List<Object[]> countReactionsForPosts(@Param("postIds") List<String> postIds);

}
