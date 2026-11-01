package com.kartik.blog_platform_api.controller;

import com.kartik.blog_platform_api.dto.PostRequestDto;
import com.kartik.blog_platform_api.dto.PostResponseDto;
import com.kartik.blog_platform_api.service.PostService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private  final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public PostResponseDto createPost(@Valid @RequestBody
                                      PostRequestDto requestDto){
        return postService.createPost(requestDto);

    }

    @GetMapping
    public Page<PostResponseDto> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "newest") String sort,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String title) {

        Pageable pageable = PageRequest.of(page, size);

        if (title != null || categoryId != null || tagId != null) {
            return postService.searchAndFilterPosts(
                    title, categoryId, tagId, pageable, sort);
        }

        return postService.getAllPosts(pageable, sort);
    }
    @GetMapping("/search")
    public List<PostResponseDto>searchPostsByTitle(
            @RequestParam String title){
        return postService.searchPostsByTitle(title);
    }

    @GetMapping("/{id}")
    public PostResponseDto getPostById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public PostResponseDto updatePost(
            @PathVariable Long id,
            @Valid @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(id, requestDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id){
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
