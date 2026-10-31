package com.kartik.blog_platform_api.controller;

import com.kartik.blog_platform_api.dto.CategoryRequestDto;
import com.kartik.blog_platform_api.dto.CategoryResponseDto;
import com.kartik.blog_platform_api.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryResponseDto createCategory(
            @Valid @RequestBody CategoryRequestDto requestDto) {

        return categoryService.createCategory(requestDto);
    }

    @GetMapping
    public List<CategoryResponseDto> getAllCategories() {
        return categoryService.getAllCategories();
    }
}