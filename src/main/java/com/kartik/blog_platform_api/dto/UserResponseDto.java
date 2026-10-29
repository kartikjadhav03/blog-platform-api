package com.kartik.blog_platform_api.dto;

public record UserResponseDto(
        Long id,
        String name,
        String email
) {
}