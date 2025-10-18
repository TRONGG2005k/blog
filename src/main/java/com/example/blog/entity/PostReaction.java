package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import static lombok.AccessLevel.*;

@Entity
@Table(
        name = "post_reaction",
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"user_id","post_id","type"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = PRIVATE)
public class PostReaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 37)
    String id;

    @Column(nullable = false)
    String type;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Builder.Default
    LocalDateTime createdAt = LocalDateTime.now();
}
