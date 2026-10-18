package com.example.identity_server.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String fullName;
    private Integer age;
    private Integer enabled;
    private Long donViId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
