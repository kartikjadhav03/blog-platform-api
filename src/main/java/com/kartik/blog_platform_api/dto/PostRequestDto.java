package com.kartik.blog_platform_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

public record PostRequestDto(
        @NotBlank(message = "Title cannot be blank")
        String title,

        @NotBlank(message = "Content cannot be blank")
        String content,

        @NotNull(message = "Author ID cannot be null")
        @Positive(message = "Author ID must be positive")
        Long authorId,

        Long categoryId,

        Set<Long> tagIds
) {
}