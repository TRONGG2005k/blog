package com.example.blog.repository;

import com.example.blog.entity.Comment;
import com.example.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, String> {
    @Query("""
    SELECT c
    FROM Comment c
    WHERE c.post.id = :postId
      AND c.parent IS NULL
      AND c.isDeleted = false
    ORDER BY c.createdAt DESC
    """)
    Page<Comment> findByPostIdAndParentIsNullAndIsDeletedFalse(
            @Param("postId") String postId,
            Pageable pageable
    );

    Long countByPost(Post post);

    @Query("SELECT c.post.id, COUNT(c) FROM Comment c WHERE c.isDeleted = false and c.post.id IN :postIds GROUP BY c.post.id")
    List<Object[]> countCommentsForPosts(@Param("postIds") List<String> postIds);
}
