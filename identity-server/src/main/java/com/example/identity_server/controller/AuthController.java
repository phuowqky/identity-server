package com.example.identity_server.controller;

import com.example.identity_server.constant.ApiPath;
import com.example.identity_server.constant.MessageConstant;
import com.example.identity_server.dto.request.AuthRequest;
import com.example.identity_server.dto.response.AuthResponse;
import com.example.identity_server.response.BaseResponse;
import com.example.identity_server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.API_V0 + "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public BaseResponse<AuthResponse> login(
            @Valid @RequestBody AuthRequest request,
            HttpServletRequest httpRequest) {
        String clientIp = extractClientIp(httpRequest);
        return BaseResponse.success(MessageConstant.Auth.LOGIN_SUCCESS,
                authService.login(request, clientIp));
    }

    public String extractClientIp(HttpServletRequest request){

        //X-Forwarded-For có nhiều IP: "client, proxy1, proxy2"
        String xff = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xff)) {
            /**
             * split(",") tách ra -> lấy [0] = IP thật của user -> trim() xoá khoảng trắng
             * mục đích lấy IP thật của người dùng, không phải proxy
             */
            return xff.split(",")[0].trim();
        }

        // Kiểm tra chuỗi có giá trị hợp lệ hay không
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return realIp.trim();
        }

        return request.getRemoteAddr();
    }
}
