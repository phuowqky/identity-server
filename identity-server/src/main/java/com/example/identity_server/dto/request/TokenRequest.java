package com.example.identity_server.dto.request;

import jakarta.validation.constraints.NotBlank;

/** Body của POST /api/v0/sso/token */
public record TokenRequest(
        @NotBlank String code,
        @NotBlank String appId
) {
}