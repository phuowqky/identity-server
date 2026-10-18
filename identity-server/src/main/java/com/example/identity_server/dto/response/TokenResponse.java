package com.example.identity_server.dto.response;

/** Kết quả của POST /api/v0/sso/token */
public record TokenResponse(
        String token,
        String tokenType,
        long expiresInMs
) {
    public static TokenResponse bearer(String token, long expiresInMs) {
        return new TokenResponse(token, "Bearer", expiresInMs);
    }
}