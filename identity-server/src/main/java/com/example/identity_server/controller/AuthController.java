//package com.example.identity_server.controller;
//
//import com.example.identity_server.constant.ApiPath;
//import com.example.identity_server.constant.MessageConstant;
//import com.example.identity_server.dto.request.LoginRequest;
//import com.example.identity_server.dto.request.RegisterRequest;
//import com.example.identity_server.dto.response.LoginResponse;
//import com.example.identity_server.dto.response.UserResponseDTO;
//import com.example.identity_server.response.BaseResponse;
//import com.example.identity_server.service.AuthService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.Authentication;
//import org.springframework.util.StringUtils;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RestController
//@RequestMapping(ApiPath.API_V0 + "/auth")
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/login")
//    public BaseResponse<LoginResponse> login(
//            @Valid @RequestBody LoginRequest request,
//            HttpServletRequest httpRequest) {
//        String clientIp = extractClientIp(httpRequest);
//        return BaseResponse.success(MessageConstant.Auth.LOGIN_SUCCESS,
//                authService.login(request, clientIp));
//    }
//
//    @PostMapping("/register")
//    public BaseResponse<UserResponseDTO> register(
//            @Valid @RequestBody RegisterRequest request
//            ) {
//        return BaseResponse.success(MessageConstant.Auth.REGISTER_SUCCESS, authService.register(request));
//    }
//
//    public String extractClientIp(HttpServletRequest request){
//
//        //X-Forwarded-For có nhiều IP: "client, proxy1, proxy2"
//        String xff = request.getHeader("X-Forwarded-For");
//        if (StringUtils.hasText(xff)) {
//            /**
//             * split(",") tách ra -> lấy [0] = IP thật của user -> trim() xoá khoảng trắng
//             * mục đích lấy IP thật của người dùng, không phải proxy
//             */
//            return xff.split(",")[0].trim();
//        }
//
//        // Kiểm tra chuỗi có giá trị hợp lệ hay không
//        String realIp = request.getHeader("X-Real-IP");
//        if (StringUtils.hasText(realIp)) {
//            return realIp.trim();
//        }
//
//        return request.getRemoteAddr();
//    }
//
//    @GetMapping("/me")
//    public BaseResponse<String> me(Authentication authentication) {
//
//        return BaseResponse.success(
//                "Get current user successfully",
//                authentication.getName()
//        );
//    }
//}

package com.example.identity_server.controller;

import com.example.identity_server.constant.ApiPath;
import com.example.identity_server.constant.MessageConstant;
import com.example.identity_server.dto.request.LoginRequest;
import com.example.identity_server.dto.request.RegisterRequest;
import com.example.identity_server.dto.response.LoginResponse;
import com.example.identity_server.dto.response.UserResponseDTO;
import com.example.identity_server.response.BaseResponse;
import com.example.identity_server.security.SsoSessionCookie;
import com.example.identity_server.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.API_V0 + "/auth")
public class AuthController {

    private final AuthService authService;
    private final SsoSessionCookie ssoSessionCookie;

    /**
     * Đăng nhập. Ngoài JWT nội bộ, set thêm cookie SSO_SESSION
     * để các app khác đăng nhập mà không cần gõ lại mật khẩu.
     */
    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest,
            HttpServletResponse httpResponse) {
        String clientIp = extractClientIp(httpRequest);
        LoginResponse loginResponse = authService.login(request, clientIp);
        ssoSessionCookie.create(httpResponse, loginResponse.getUsername());
        return BaseResponse.success(MessageConstant.Auth.LOGIN_SUCCESS, loginResponse);
    }

    @PostMapping("/register")
    public BaseResponse<UserResponseDTO> register(@Valid @RequestBody RegisterRequest request) {
        return BaseResponse.success(MessageConstant.Auth.REGISTER_SUCCESS, authService.register(request));
    }

    @GetMapping("/me")
    public BaseResponse<String> me(Authentication authentication) {
        return BaseResponse.success("Get current user successfully", authentication.getName());
    }

    private String extractClientIp(HttpServletRequest request) {
        // X-Forwarded-For có thể có nhiều IP: "client, proxy1, proxy2" -> lấy IP đầu tiên
        String xff = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(xff)) {
            return xff.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (StringUtils.hasText(realIp)) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }
}