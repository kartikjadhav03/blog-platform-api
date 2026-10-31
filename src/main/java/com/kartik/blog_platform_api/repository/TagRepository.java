package com.kartik.blog_platform_api.repository;

import com.kartik.blog_platform_api.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, Long> {
}