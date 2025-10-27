package com.example.blog.repository;

import com.example.blog.dto.response.PostAdminResponse;
import com.example.blog.entity.Post;
import com.example.blog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findByUser(User user);

    @Query(value = "select p from Post p where p.isDeleted = false")
    List<Post> findAllPost();

    Page<Post> findAllByIsDeletedFalse(Pageable pageable);

    Page<Post> findAllByIsDeletedFalseAndUser_Username(String username, Pageable pageable);

//    @Query("""
//        SELECT new com.example.blog.dto.response.PostAdminResponse(
//            p.id,
//            p.caption,
//            p.user.username,
//            SIZE(p.comments),
//            SIZE(p.reactions),
//            (SELECT m.url FROM Media m WHERE m.post = p),
//            p.createdAt,
//            p.updatedAt
//        )
//        FROM Post p
//        WHERE p.isDeleted = false
//    """)
//    Page<PostAdminResponse> findPostsForAdmin(Pageable pageable);

}
