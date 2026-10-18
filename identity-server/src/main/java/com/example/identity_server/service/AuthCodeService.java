package com.example.identity_server.service;

import com.example.identity_server.config.ClientAppProperties;
import com.example.identity_server.entity.AuthCode;
import com.example.identity_server.exception.SsoException;
import com.example.identity_server.repository.AuthCodeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

/** Mã dùng một lần đổi lấy JWT. Lưu bảng AUTH_CODE, sống sso.code-ttl-seconds. */
@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final AuthCodeRepository repository;
    private final ClientAppProperties props;
    private final SecureRandom random = new SecureRandom();

    @Transactional
    public String issue(String appId, String username, String redirectUri) {
        AuthCode code = new AuthCode();
        code.setCode(randomToken());
        code.setAppId(appId);
        code.setUsername(username);
        code.setRedirectUri(redirectUri);
        code.setExpiresAt(Instant.now().plusSeconds(props.getCodeTtlSeconds()));
        repository.save(code);
        return code.getCode();
    }

    /** Khoá dòng, kiểm tra, đánh dấu đã dùng. Ném SsoException nếu sai, hết hạn, đã dùng, hoặc khác app. */
    @Transactional
    public AuthCode consume(String code, String appId) {
        AuthCode c = repository.findByCodeForUpdate(code)
                .orElseThrow(() -> new SsoException("Code không hợp lệ"));
        if (c.isUsed() || c.isExpired() || !c.getAppId().equals(appId)) {
            throw new SsoException("Code không hợp lệ hoặc đã hết hạn");
        }
        c.setUsed(1);
        return c;
    }

    private String randomToken() {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}