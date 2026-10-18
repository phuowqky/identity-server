package com.example.identity_server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/** Một ứng dụng được phép dùng SSO. Bảng CLIENT_APP + CLIENT_APP_REDIRECT_URI. */
@Entity
@Table(name = "CLIENT_APP")
@Getter
@Setter
@NoArgsConstructor
public class ClientApp {

    @Id
    @Column(name = "APP_ID", length = 64)
    private String appId;

    @Column(name = "NAME", nullable = false, length = 200)
    private String name;

    /** Secret ký JWT cho app này. Phải trùng jwt.secret-key trong backend của app. */
    @Column(name = "JWT_SECRET", nullable = false, length = 512)
    private String jwtSecret;

    @Column(name = "TOKEN_TTL_MS", nullable = false)
    private long tokenTtlMs;

    @Column(name = "ENABLED", nullable = false)
    private int enabled = 1;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "CLIENT_APP_REDIRECT_URI", joinColumns = @JoinColumn(name = "APP_ID"))
    @Column(name = "REDIRECT_URI", nullable = false, length = 1000)
    private List<String> redirectUris = new ArrayList<>();
}