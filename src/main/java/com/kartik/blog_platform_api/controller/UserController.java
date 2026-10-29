package com.kartik.blog_platform_api.controller;

import com.kartik.blog_platform_api.dto.UserRequestDto;
import com.kartik.blog_platform_api.dto.UserResponseDto;
import com.kartik.blog_platform_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(
            @Valid @RequestBody UserRequestDto requestDto) {
        return userService.createUser(requestDto);
    }
}