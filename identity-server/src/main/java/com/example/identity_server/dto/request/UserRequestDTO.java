package com.example.identity_server.dto.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRequestDTO {
    private String username;
    private String email;
    private String password;
    private String fullName;
    private Integer age;
    private Long donViId;
}
