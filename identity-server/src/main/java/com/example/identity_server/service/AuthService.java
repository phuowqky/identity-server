package com.example.identity_server.service;

import com.example.identity_server.dto.request.AuthRequest;
import com.example.identity_server.dto.response.AuthResponse;
import com.example.identity_server.entity.User;
import com.example.identity_server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;

    public AuthResponse login(AuthRequest authRequest, String clientIp) {

        User user = userRepository.findByUsername(authRequest.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("Username không tồn tại"));

        if (!user.getPassword().equals(authRequest.getPassword())) {
            throw new RuntimeException("Sai mật khẩu");
        }

        if (user.getEnabled() != 1) {
            throw new RuntimeException("Tài khoản đã bị khóa");
        }

        return new AuthResponse(user.getUsername());
    }
}
