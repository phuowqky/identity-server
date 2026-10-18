package com.example.identity_server.dto.response;

import com.example.identity_server.response.BaseResponse;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String accessToken;
    private String username;
}
