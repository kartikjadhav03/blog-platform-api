package com.kartik.blog_platform_api.dto;

import com.kartik.blog_platform_api.entity.PostStatus;
import java.time.LocalDateTime;
import java.util.List;

public record PostResponseDto(
        Long id,
        String title,
        String content,
        String authorName,
        String categoryName,
        List<String> tagNames,
        PostStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}