package com.kartik.blog_platform_api.controller;

import com.kartik.blog_platform_api.dto.CommentRequestDto;
import com.kartik.blog_platform_api.dto.CommentResponseDto;
import com.kartik.blog_platform_api.entity.Comment;
import com.kartik.blog_platform_api.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponseDto createComment(
            @PathVariable Long postId,
            @Valid @RequestBody CommentRequestDto requestDto) {

        return commentService.createComment(
                requestDto.getContent(),
                postId,
                requestDto.getAuthorId()
        );
    }

    @GetMapping
    public List<CommentResponseDto> getCommentsByPostId(
            @PathVariable Long postId) {

        return commentService.getCommentsByPostId(postId);
    }
}