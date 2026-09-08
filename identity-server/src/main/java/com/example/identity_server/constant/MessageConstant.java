package com.example.identity_server.constant;

import lombok.Data;
import lombok.RequiredArgsConstructor;


public class MessageConstant {

    public static class User {
        public static final String NOT_FOUND = "User not found with id: %s";
        public static final String CREATED = "User created successfully";
        public static final String UPDATED = "User updated successfully";
        public static final String DELETED = "User deleted successfully";
    }

    public static class Action {
        public static final String NOT_FOUND = "Action không tồn tại với id: %s";
        public static final String CREATED = "Tạo action thành công";
        public static final String UPDATED = "Cập nhật action thành công";
        public static final String DELETED = "Xóa action thành công";
        public static final String DELETEDALL = "Xóa tất cả action thành công";
    }

    public static class Validation {
        // Common validation messages - dùng chung cho tất cả API
        public static final String REQUIRED = "%s is required";
        public static final String INVALID_FORMAT = "Invalid %s format";
        public static final String MUST_BE_POSITIVE = "%s must be positive";
        public static final String MIN_LENGTH = "%s must be at least %d characters";
        public static final String MAX_LENGTH = "%s must not exceed %d characters";
        public static final String VALIDATION_FAILED = "Validation failed";

        // Specific field names - tên field cụ thể
        public static final String FIELD_NAME = "Name";
        public static final String FIELD_EMAIL = "Email";
        public static final String FIELD_AGE = "Age";
        public static final String FIELD_PASSWORD = "Password";
        public static final String FIELD_PHONE = "Phone";
    }

    public static class Common {
        public static final String SUCCESS = "Success";
        public static final String INTERNAL_ERROR = "Internal server error: %s";
    }

    public static class Auth {
        public static final String LOGIN_SUCCESS = "Đăng nhập thành công";
        public static final String REGISTER_SUCCESS = "Đăng ký thành công";
        public static final String INVALID_CREDENTIALS = "Tên đăng nhập hoặc mật khẩu không đúng";
        public static final String TOKEN_EXPIRED = "Token đã hết hạn";
        public static final String ACCESS_DENIED = "Bạn không có quyền truy cập tài nguyên này";
        public static final String UNAUTHORIZED = "Bạn cần đăng nhập để truy cập";
    }
}

