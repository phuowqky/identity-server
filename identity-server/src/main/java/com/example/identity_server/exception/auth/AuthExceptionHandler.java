//package com.example.identity_server.exception.auth;
//
//import com.example.identity_server.response.BaseResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class AuthExceptionHandler {
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<BaseResponse<Object>> handleBadCredentials(BadCredentialsException ex) {
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(BaseResponse.error("Sai tên đăng nhập hoặc mật khẩu"));
//    }
//}
