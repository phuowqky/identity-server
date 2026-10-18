package com.example.identity_server.service;

import com.example.identity_server.dto.request.UserRequestDTO;
import com.example.identity_server.dto.response.UserResponseDTO;
import com.example.identity_server.entity.User;
import com.example.identity_server.exception.ResourceNotFoundException;
import com.example.identity_server.mapper.UserMapper;
import com.example.identity_server.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@RequiredArgsConstructor
@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        if (userRepository.findByUsername(userRequestDTO.getUsername()).isPresent()) {
            throw new RuntimeException("User name đã tồn tại");
        }
        User user = userMapper.toEntity(userRequestDTO);
        user.setUsername(user.getUsername().toUpperCase());
        User saved = userRepository.save(user);
        log.info("Đã tạo user: {}", saved.getEmail());
        return userMapper.toDTO(saved);
    }

    @Transactional
    public UserResponseDTO update(Long id, UserRequestDTO requestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy user"));

        userMapper.updateEntityFromDTO(requestDTO, user);

        if (requestDTO.getUsername() != null) {
            user.setUsername(user.getUsername().toUpperCase());
        }

        User saved = userRepository.save(user);
        log.info("Đã cập nhật user: {}", saved.getEmail());

        return userMapper.toDTO(saved);
    }


}
