package com.kartik.blog_platform_api.dto;

public record CommentResponseDto(
        Long id,
        String content,
        Long postId,
        String authorName
) {
}