package com.kartik.blog_platform_api.repository;

import com.kartik.blog_platform_api.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Long> {

      List<Post> findByTitleContainingIgnoreCase(String title);
}
