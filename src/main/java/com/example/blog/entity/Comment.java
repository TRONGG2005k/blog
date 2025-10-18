package com.example.blog.entity;

import com.example.blog.Enum.CommentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "comment")
public class Comment extends Base{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 37)
    String id;

    @Column(nullable = false, length = 500)
    String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    Comment parent;

    @OneToMany(mappedBy = "parent")
    List<Comment> replies;

    @OneToMany(mappedBy = "comment")
    List<CommentReaction> reactions;
}
