package com.example.identity_server.dto.response;

import com.example.identity_server.response.BaseResponse;
import lombok.*;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse extends BaseResponse {
    private String username;
}
