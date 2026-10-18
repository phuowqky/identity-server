package com.example.identity_server.scheduler;

import com.example.identity_server.repository.AuthCodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/** Dọn bảng AUTH_CODE: xoá code đã dùng hoặc hết hạn, mỗi 5 phút. */
@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class AuthCodeCleanupJob {

    private final AuthCodeRepository repository;

    @Scheduled(fixedDelayString = "${sso.cleanup-interval-ms:300000}")
    @Transactional
    public void cleanup() {
        int deleted = repository.deleteExpiredOrUsed(Instant.now());
        if (deleted > 0) {
            log.info("AuthCode cleanup: deleted {} rows", deleted);
        }
    }
}