package com.example.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "role")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
            @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 37)
    String id;

    @Column(nullable = false)
    String name;

    String description;

    @ManyToMany(mappedBy = "roles")
    Set<User> users;
}
