package com.kartik.blog_platform_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CommentRequestDto {

    @NotBlank(message = "content cannot be blank")
    private String content ;

    @NotNull(message = "Author ID cannot be null")
    @Positive(message = "Author ID must be positive")
    private Long authorId;

    public String getContent() {
        return content;
    }

    public Long getAuthorId() {
        return authorId;
    }

}
