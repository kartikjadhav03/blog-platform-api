package com.kartik.blog_platform_api.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;

public record PostRequestDto(

        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Content cannot be blank")
        String content,

        Long categoryId,

        Set<Long> tagIds

) {
}