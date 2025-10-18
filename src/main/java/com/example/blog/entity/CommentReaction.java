package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.PRIVATE;
@Entity
@Table(
        name = "comment_reaction",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id","comment_id","type"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)

public class CommentReaction{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 37)
    String id;

    @Column(nullable = false)
    String type;

    @ManyToOne
    @JoinColumn(name = "comment_id", nullable = false)
    Comment comment;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    LocalDateTime createdAt = LocalDateTime.now();
}
