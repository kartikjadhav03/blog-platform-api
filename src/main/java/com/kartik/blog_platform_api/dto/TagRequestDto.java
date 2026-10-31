package com.kartik.blog_platform_api.dto;

import jakarta.validation.constraints.NotBlank;

public class TagRequestDto {

    @NotBlank(message = "Tag name cannot be blank")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}