package com.kartik.blog_platform_api.controller;

import com.kartik.blog_platform_api.dto.TagRequestDto;
import com.kartik.blog_platform_api.dto.TagResponseDto;
import com.kartik.blog_platform_api.service.TagService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @PostMapping
    public TagResponseDto createTag(
            @Valid @RequestBody TagRequestDto requestDto) {
        return tagService.createTag(requestDto);
    }

    @GetMapping
    public List<TagResponseDto> getAllTags() {
        return tagService.getAllTags();
    }
}