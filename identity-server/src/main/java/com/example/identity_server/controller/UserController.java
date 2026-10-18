package com.example.identity_server.controller;

import com.example.identity_server.constant.ApiPath;


import com.example.identity_server.dto.request.UserRequestDTO;
import com.example.identity_server.dto.response.UserResponseDTO;
import com.example.identity_server.response.BaseResponse;
import com.example.identity_server.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiPath.API_V0 + "/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/create")
    public BaseResponse<UserResponseDTO> create(
            @RequestBody UserRequestDTO userRequestDTO) {
        return BaseResponse.success(userService.create(userRequestDTO));
    }

    @PutMapping("/update")
    public BaseResponse<UserResponseDTO> update(
            @PathVariable Long id,
            @RequestBody UserRequestDTO userRequestDTO
    ){
        return BaseResponse.success(userService.update(id, userRequestDTO));
    }


}
