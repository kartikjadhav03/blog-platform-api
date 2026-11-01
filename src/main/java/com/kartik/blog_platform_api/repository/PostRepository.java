package com.kartik.blog_platform_api.repository;

import com.kartik.blog_platform_api.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

      List<Post> findByTitleContainingIgnoreCase(String title);

      Page<Post> findByCategoryId(Long categoryId, Pageable pageable);

      Page<Post> findByTagsId(Long tagId, Pageable pageable);

      @Query("""
    SELECT DISTINCT p FROM Post p
    LEFT JOIN p.tags t
    WHERE (:title IS NULL OR LOWER(p.title) LIKE LOWER(CONCAT('%', :title, '%')))
      AND (:categoryId IS NULL OR p.category.id = :categoryId)
      AND (:tagId IS NULL OR t.id = :tagId)
    """)
      Page<Post> searchAndFilterPosts(
              @Param("title") String title,
              @Param("categoryId") Long categoryId,
              @Param("tagId") Long tagId,
              Pageable pageable
      );
}
