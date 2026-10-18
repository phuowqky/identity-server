package com.example.identity_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/** Mã dùng một lần để đổi lấy JWT. Bảng AUTH_CODE. */
@Entity
@Table(name = "AUTH_CODE")
@Getter
@Setter
@NoArgsConstructor
public class AuthCode {

    @Id
    @Column(name = "CODE", length = 128)
    private String code;

    @Column(name = "APP_ID", nullable = false, length = 64)
    private String appId;

    @Column(name = "USERNAME", nullable = false, length = 200)
    private String username;

    @Column(name = "REDIRECT_URI", nullable = false, length = 1000)
    private String redirectUri;

    @Column(name = "EXPIRES_AT", nullable = false)
    private Instant expiresAt;

    @Column(name = "USED", nullable = false)
    private int used = 0;

    @Column(name = "CREATED_AT", nullable = false)
    private Instant createdAt = Instant.now();

    public boolean isUsed() {
        return used == 1;
    }

    public boolean isExpired() {
        return expiresAt.isBefore(Instant.now());
    }
}