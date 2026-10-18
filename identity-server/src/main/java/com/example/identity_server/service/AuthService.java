//package com.example.identity_server.service;
//
//import com.example.identity_server.dto.request.LoginRequest;
//import com.example.identity_server.dto.request.RegisterRequest;
//import com.example.identity_server.dto.response.LoginResponse;
//import com.example.identity_server.dto.response.RegisterResponse;
//import com.example.identity_server.dto.response.UserResponseDTO;
//import com.example.identity_server.entity.User;
//import com.example.identity_server.mapper.UserMapper;
//import com.example.identity_server.repository.UserRepository;
//import com.example.identity_server.security.JwtService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class AuthService {
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final UserMapper userMapper;
//    private final JwtService jwtService;
//
//    public LoginResponse login(LoginRequest loginRequest, String clientIp) {
//
//        User user = userRepository.findByUsername(loginRequest.getUsername())
//                .orElseThrow(() ->
//                        new RuntimeException("Username không tồn tại"));
//
//
//        /**
//         * auth.getPassword: mk lúc gửi req lên
//         * user.getPassword mk đã được BCrypt mã hóa đang lưu trong database.
//         */
//
//        if(!passwordEncoder.matches(
//                loginRequest.getPassword(),
//                user.getPassword()
//        )){
//            throw new RuntimeException("Sai mật khẩu");
//        }
//
//        if (user.getEnabled() != 1) {
//            throw new RuntimeException("Tài khoản đã bị khóa");
//        }
//        String accessToken = jwtService.generateToken(user.getUsername());
//
//        return new LoginResponse(accessToken, user.getUsername());
//    }
//
//    public UserResponseDTO register(RegisterRequest request) {
//
//        if (userRepository.existsByUsername(request.getUsername())) {
//            throw new RuntimeException("Username already exists");
//        }
//
//        if (userRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        User user = new User();
//
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//
//        // Không lưu password dạng plain text
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//
//        User savedUser = userRepository.save(user);
//
//        return userMapper.toResponse(savedUser);
//    }
//}


package com.example.identity_server.service;

import com.example.identity_server.dto.request.LoginRequest;
import com.example.identity_server.dto.request.RegisterRequest;
import com.example.identity_server.dto.response.LoginResponse;
import com.example.identity_server.dto.response.UserResponseDTO;
import com.example.identity_server.entity.User;
import com.example.identity_server.exception.AuthException;
import com.example.identity_server.mapper.UserMapper;
import com.example.identity_server.repository.UserRepository;
import com.example.identity_server.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private static final String BAD_CREDENTIALS = "Sai tên đăng nhập hoặc mật khẩu";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest loginRequest, String clientIp) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> {
                    log.warn("Login failed (no user): username={} ip={}", loginRequest.getUsername(), clientIp);
                    return new AuthException(BAD_CREDENTIALS);
                });

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Login failed (bad password): username={} ip={}", user.getUsername(), clientIp);
            throw new AuthException(BAD_CREDENTIALS);
        }

        if (user.getEnabled() != 1) {
            log.warn("Login blocked (disabled): username={} ip={}", user.getUsername(), clientIp);
            throw new AuthException("Tài khoản đã bị khóa");
        }

        log.info("Login ok: username={} ip={}", user.getUsername(), clientIp);
        String accessToken = jwtService.generateToken(user.getUsername());
        return new LoginResponse(accessToken, user.getUsername());
    }

    public UserResponseDTO register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AuthException("Username đã tồn tại");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException("Email đã tồn tại");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        return userMapper.toResponse(userRepository.save(user));
    }
}