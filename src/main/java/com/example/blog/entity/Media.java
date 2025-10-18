package com.example.blog.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Entity
@Table(name = "media")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Media extends Base {
    @Id
    @Column(length = 37)
    String id;

    String url;
    Long size;
    String type;
    String name;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = true)
            @JsonBackReference
    Post post;
}
