package com.example.identity_server.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Đọc block `sso:` trong application.yml.
 * - Cấu hình cookie/session/code dùng chung.
 * - Danh sách `apps` chỉ dùng để SEED vào bảng CLIENT_APP lúc khởi động (xem ClientAppService.seedFromConfig).
 *   Nguồn sự thật khi chạy là bảng CLIENT_APP trong Oracle.
 */
@Configuration
@ConfigurationProperties(prefix = "sso")
@Getter
@Setter
public class ClientAppProperties {

    private String cookieName = "SSO_SESSION";
    private boolean cookieSecure = true;
    private long sessionTtlMs = 8 * 60 * 60 * 1000L;
    private int codeTtlSeconds = 60;
    private String loginPage = "/login";
    private List<String> allowedOrigins = new ArrayList<>();
    private List<SeedApp> apps = new ArrayList<>();

    @Getter
    @Setter
    public static class SeedApp {
        private String appId;
        private String name;
        private List<String> redirectUris = new ArrayList<>();
        private String jwtSecret;
        private long tokenTtlMs;
    }
}