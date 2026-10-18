//package com.example.identity_server.security;
//
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.crypto.SecretKey;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//
//@Service
//public class JwtService {
//
//    /**
//     * Lấy JWT secret từ application.yml
//     *
//     * Không hard code secret trực tiếp trong Java.
//     */
//    @Value("${jwt.secret-key}")
//    private String secretKey;
//
//    /**
//     * Thời gian JWT tồn tại.
//     *
//     * Giá trị được lấy từ application.yml
//     */
//    @Value("${jwt.expiration}")
//    private long expirationTime;
//
//    /**
//     * Tạo SecretKey dùng để ký JWT
//     */
//    private SecretKey getSigningKey() {
//        return Keys.hmacShaKeyFor(
//                secretKey.getBytes(StandardCharsets.UTF_8)
//        );
//    }
//
//    /**
//     * Tạo Access Token cho user
//     */
//    public String generateToken(String username){
//        Date now = new Date();
//
//        Date expiration = new Date(
//                now.getTime() + expirationTime
//        );
//
//        return Jwts.builder()
//
//                // Username của user
//                .subject(username)
//
//                // Thời điểm tạo token
//                .issuedAt(now)
//
//                // Thời điểm token hết hạn
//                .expiration(expiration)
//
//                // Ký JWT
//                .signWith(getSigningKey())
//
//                // Chuyển JWT thành String
//                .compact();
//    }
//
//    /**
//     * LẤY JWT
//     */
//
//    public String extractUsername(String token) {
//
//        SecretKey key = Keys.hmacShaKeyFor(
//                secretKey.getBytes(StandardCharsets.UTF_8)
//        );
//
//        return Jwts.parser()
//                .verifyWith(key)
//                .build()
//                .parseSignedClaims(token)
//                .getPayload()
//                .getSubject();
//    }
//
//    public boolean isTokenValid(String token) {
//
//        try {
//            SecretKey key = Keys.hmacShaKeyFor(
//                    secretKey.getBytes(StandardCharsets.UTF_8)
//            );
//
//            Jwts.parser()
//                    .verifyWith(key)
//                    .build()
//                    .parseSignedClaims(token);
//
//            return true;
//
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}

package com.example.identity_server.security;

import com.example.identity_server.entity.ClientApp;
import com.example.identity_server.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private static final String SESSION_TYPE = "sso_session";

    /** Secret ký JWT nội bộ của identity server. Lấy từ application.yml, không hard code. */
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /** JWT nội bộ (API /me của identity server). */
    public String generateToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * JWT đặt trong cookie SSO_SESSION. Sống theo sso.session-ttl-ms (dài hơn token nội bộ),
     * mang claim typ=sso_session để không dùng nhầm làm access token.
     */
    public String generateSessionToken(String username, long ttlMs) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .claim("typ", SESSION_TYPE)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + ttlMs))
                .signWith(getSigningKey())
                .compact();
    }

    /** Đọc username từ session token; Optional.empty nếu token sai, hết hạn hoặc không phải session token. */
    public Optional<String> extractSessionUsername(String sessionToken) {
        try {
            Claims claims = Jwts.parser().verifyWith(getSigningKey()).build()
                    .parseSignedClaims(sessionToken).getPayload();
            if (!SESSION_TYPE.equals(claims.get("typ", String.class))) {
                return Optional.empty();
            }
            return Optional.ofNullable(claims.getSubject());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * JWT phát cho một app trong hệ thống SSO.
     * Ký bằng secret của app đó (trùng jwt.secret-key trong backend của app) nên backend app không cần sửa.
     * Claims phải khớp với những gì backend app đang đọc: hiện tại subject = username.
     */
    public String generateTokenForApp(User user, ClientApp app) {
        SecretKey appKey = Keys.hmacShaKeyFor(app.getJwtSecret().getBytes(StandardCharsets.UTF_8));
        Date now = new Date();
        return Jwts.builder()
                .subject(user.getUsername())
                .claim("email", user.getEmail())
                .claim("app", app.getAppId())
                .issuedAt(now)
                .expiration(new Date(now.getTime() + app.getTokenTtlMs()))
                .signWith(appKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload().getSubject();
    }

    /** Access token nội bộ hợp lệ (và không phải session token). */
    public boolean isTokenValid(String token) {
        try {
            Claims claims = Jwts.parser().verifyWith(getSigningKey()).build()
                    .parseSignedClaims(token).getPayload();
            return !SESSION_TYPE.equals(claims.get("typ", String.class));
        } catch (Exception e) {
            return false;
        }
    }
}