package com.kartik.blog_platform_api.repository;

import com.kartik.blog_platform_api.entity.Category;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
