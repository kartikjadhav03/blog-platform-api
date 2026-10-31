package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.CategoryRequestDto;
import com.kartik.blog_platform_api.dto.CategoryResponseDto;
import com.kartik.blog_platform_api.entity.Category;
import com.kartik.blog_platform_api.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public CategoryResponseDto createCategory(CategoryRequestDto requestDto) {
        Category category = new Category();
        category.setName(requestDto.getName());

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponseDto(
                savedCategory.getId(),
                savedCategory.getName()
        );
    }

    public List<CategoryResponseDto> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(category -> new CategoryResponseDto(
                        category.getId(),
                        category.getName()
                ))
                .toList();
    }
}