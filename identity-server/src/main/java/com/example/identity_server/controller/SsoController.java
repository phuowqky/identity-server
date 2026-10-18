package com.example.identity_server.controller;

import com.example.identity_server.constant.ApiPath;
import com.example.identity_server.dto.request.TokenRequest;
import com.example.identity_server.dto.response.TokenResponse;
import com.example.identity_server.response.BaseResponse;
import com.example.identity_server.security.SsoSessionCookie;
import com.example.identity_server.service.SsoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPath.API_V0 + "/sso")
public class SsoController {

    private final SsoService ssoService;
    private final SsoSessionCookie ssoSessionCookie;

    /**
     * Bước 1 của luồng SSO. App redirect trình duyệt tới đây.
     * Có session -> redirect về app kèm code. Chưa có -> sang trang login, xong quay lại đây.
     */
    @GetMapping("/authorize")
    public void authorize(@RequestParam String appId,
                          @RequestParam String redirectUri,
                          @RequestParam String state,
                          HttpServletRequest request,
                          HttpServletResponse response) throws IOException {
        SsoService.AuthorizeResult result =
                ssoService.authorize(appId, redirectUri, state, ssoSessionCookie.currentUsername(request));
        response.sendRedirect(result.redirectUrl());
    }

    /** Bước 2. App đổi code lấy JWT ký bằng secret của chính app đó. */
    @PostMapping("/token")
    public BaseResponse<TokenResponse> token(@Valid @RequestBody TokenRequest request) {
        return BaseResponse.success("Token issued", ssoService.exchangeToken(request.code(), request.appId()));
    }

    /** Đăng xuất khỏi SSO: xoá cookie. App vẫn phải tự xoá JWT cục bộ. */
    @PostMapping("/logout")
    public BaseResponse<Void> logout(HttpServletResponse response) {
        ssoSessionCookie.clear(response);
        return BaseResponse.success("Logged out", null);
    }
}