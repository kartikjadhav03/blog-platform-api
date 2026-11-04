package com.kartik.blog_platform_api.dto;

public record LoginResponseDto(
        String token,
        UserResponseDto user
) {
}