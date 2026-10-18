package com.example.identity_server.repository;

import com.example.identity_server.entity.AuthCode;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCode, String> {

    /** Khoá dòng khi consume để hai request cùng một code không cùng thành công. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from AuthCode c where c.code = :code")
    Optional<AuthCode> findByCodeForUpdate(@Param("code") String code);

    @Modifying
    @Query("delete from AuthCode c where c.expiresAt < :now or c.used = 1")
    int deleteExpiredOrUsed(@Param("now") Instant now);
}