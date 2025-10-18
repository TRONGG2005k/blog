package com.example.blog.redis;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    String username; // key
    @Indexed
    String jwtID;    // value
    @TimeToLive
    Long ttl;
}
