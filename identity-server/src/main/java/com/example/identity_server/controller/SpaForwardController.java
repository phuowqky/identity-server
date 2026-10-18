package com.example.identity_server.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Thay cho LoginPageController. Angular identity-portal được build vào static/,
 * dùng path routing nên mọi route của nó (/login, /forgot-password, ...) phải trả về index.html
 * để Angular Router tự xử lý. API (/api/**) và file tĩnh (có đuôi .js, .css, ...) không đi qua đây.
 */
@Controller
public class SpaForwardController {

    @GetMapping({"/", "/login", "/forgot-password"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }
}