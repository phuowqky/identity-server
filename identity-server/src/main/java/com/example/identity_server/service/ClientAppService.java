package com.example.identity_server.service;

import com.example.identity_server.config.ClientAppProperties;
import com.example.identity_server.entity.ClientApp;
import com.example.identity_server.exception.SsoException;
import com.example.identity_server.repository.ClientAppRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Nơi duy nhất biết app nào được dùng SSO và được redirect về đâu.
 * Nguồn sự thật là bảng CLIENT_APP; sso.apps trong YAML chỉ để seed lần đầu.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ClientAppService {

    private final ClientAppRepository repository;
    private final ClientAppProperties props;

    @Transactional(readOnly = true)
    public ClientApp findById(String appId) {
        ClientApp app = repository.findById(appId)
                .orElseThrow(() -> new SsoException("Unknown appId: " + appId));
        if (app.getEnabled() != 1) {
            throw new SsoException("App đã bị tắt: " + appId);
        }
        return app;
    }

    /** So khớp chính xác, không so khớp tiền tố, để tránh open redirect. */
    public void validateRedirectUri(ClientApp app, String redirectUri) {
        if (redirectUri == null || !app.getRedirectUris().contains(redirectUri)) {
            throw new SsoException("redirect_uri không nằm trong danh sách cho phép");
        }
    }

    /** Seed app từ application.yml vào DB nếu chưa có. Không ghi đè app đã tồn tại. */
    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void seedFromConfig() {
        for (ClientAppProperties.SeedApp seed : props.getApps()) {
            if (repository.existsById(seed.getAppId())) {
                continue;
            }
            ClientApp app = new ClientApp();
            app.setAppId(seed.getAppId());
            app.setName(seed.getName());
            app.setJwtSecret(seed.getJwtSecret());
            app.setTokenTtlMs(seed.getTokenTtlMs());
            app.setRedirectUris(seed.getRedirectUris());
            repository.save(app);
            log.info("Seeded client app: {}", seed.getAppId());
        }
    }
}