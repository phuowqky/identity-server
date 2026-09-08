package com.example.identity_server.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final long REFRESH_TOKEN_EXPIRATION = 7;

    public void save(String token, Long userId) {
        String key = "refresh_token:" + token;

        redisTemplate.opsForValue().set(
                key,
                userId,
                Duration.ofDays(REFRESH_TOKEN_EXPIRATION)
        );
    }

    public Long getUserId(String token) {
        String key = "refresh_token:" + token;

        Object value = redisTemplate.opsForValue().get(key);

        return value != null ? Long.valueOf(value.toString()) : null;
    }

    public void delete(String token) {
        String key = "refresh_token:" + token;

        redisTemplate.delete(key);
    }
}