package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.CommentResponseDto;
import com.kartik.blog_platform_api.entity.Comment;
import com.kartik.blog_platform_api.entity.Post;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.repository.CommentRepository;
import com.kartik.blog_platform_api.repository.PostRepository;
import com.kartik.blog_platform_api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public CommentService(
            CommentRepository commentRepository,
            PostRepository postRepository,
            UserRepository userRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public CommentResponseDto createComment(String content, Long postId, Long authorId){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));

        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(author);

        Comment savedComment = commentRepository.save(comment);

        return new CommentResponseDto(
                savedComment.getId(),
                savedComment.getContent(),
                savedComment.getPost().getId(),
                savedComment.getAuthor().getName()
        );


    }
    public List<CommentResponseDto> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId)
                .stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getPost().getId(),
                        comment.getAuthor().getName()
                ))
                .toList();
    }
}