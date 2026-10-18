package com.example.identity_server.service;

import com.example.identity_server.config.ClientAppProperties;
import com.example.identity_server.dto.response.TokenResponse;
import com.example.identity_server.entity.AuthCode;
import com.example.identity_server.entity.ClientApp;
import com.example.identity_server.entity.User;
import com.example.identity_server.exception.SsoException;
import com.example.identity_server.repository.UserRepository;
import com.example.identity_server.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/** Nghiệp vụ SSO. Controller chỉ gọi vào đây và chuyển kết quả thành redirect / JSON. */
@Service
@RequiredArgsConstructor
@Slf4j
public class SsoService {

    private final ClientAppProperties props;
    private final ClientAppService clientAppService;
    private final AuthCodeService authCodeService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    /** Kết quả của /authorize: URL để redirect, và cờ cho biết đó là trang login hay quay về app. */
    public record AuthorizeResult(String redirectUrl, boolean needLogin) {
    }

    public AuthorizeResult authorize(String appId, String redirectUri, String state, Optional<String> sessionUsername) {
        ClientApp app = clientAppService.findById(appId);
        clientAppService.validateRedirectUri(app, redirectUri);
        if (state == null || state.isBlank()) {
            throw new SsoException("Thiếu state");
        }

        if (sessionUsername.isEmpty()) {
            String back = "/api/v0/sso/authorize"
                    + "?appId=" + enc(appId)
                    + "&redirectUri=" + enc(redirectUri)
                    + "&state=" + enc(state);
            return new AuthorizeResult(props.getLoginPage() + "?continue=" + enc(back), true);
        }

        String code = authCodeService.issue(appId, sessionUsername.get(), redirectUri);
        log.info("SSO authorize ok: user={} app={}", sessionUsername.get(), appId);
        return new AuthorizeResult(redirectUri + "?code=" + enc(code) + "&state=" + enc(state), false);
    }

    @Transactional
    public TokenResponse exchangeToken(String code, String appId) {
        AuthCode authCode = authCodeService.consume(code, appId);
        ClientApp app = clientAppService.findById(appId);

        User user = userRepository.findByUsername(authCode.getUsername())
                .orElseThrow(() -> new SsoException("Người dùng không tồn tại"));
        if (user.getEnabled() != 1) {
            throw new SsoException("Tài khoản đã bị khóa");
        }

        String jwt = jwtService.generateTokenForApp(user, app);
        log.info("SSO token issued: user={} app={}", user.getUsername(), appId);
        return TokenResponse.bearer(jwt, app.getTokenTtlMs());
    }

    private static String enc(String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }
}