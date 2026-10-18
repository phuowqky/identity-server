package com.example.identity_server.security;

import com.example.identity_server.config.ClientAppProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Arrays;
import java.util.Optional;

/**
 * Gom toàn bộ xử lý cookie SSO_SESSION vào một chỗ.
 * Cookie chứa session token (JWT ký bởi identity server) -> stateless, không cần bảng session.
 */
@Component
@RequiredArgsConstructor
public class SsoSessionCookie {

    private final ClientAppProperties props;
    private final JwtService jwtService;

    /** Tạo session token cho user và ghi vào cookie. Gọi sau khi login thành công. */
    public void create(HttpServletResponse response, String username) {
        String token = jwtService.generateSessionToken(username, props.getSessionTtlMs());
        ResponseCookie cookie = base(token)
                .maxAge(Duration.ofMillis(props.getSessionTtlMs()))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void clear(HttpServletResponse response) {
        ResponseCookie cookie = base("").maxAge(0).build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /** Username đang đăng nhập SSO, nếu cookie có và session token còn hợp lệ. */
    public Optional<String> currentUsername(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }
        return Arrays.stream(cookies)
                .filter(c -> props.getCookieName().equals(c.getName()))
                .map(Cookie::getValue)
                .filter(v -> v != null && !v.isBlank())
                .findFirst()
                .flatMap(jwtService::extractSessionUsername);
    }

    private ResponseCookie.ResponseCookieBuilder base(String value) {
        return ResponseCookie.from(props.getCookieName(), value)
                .httpOnly(true)
                .secure(props.isCookieSecure())
                .sameSite("Lax")
                .path("/");
    }
}