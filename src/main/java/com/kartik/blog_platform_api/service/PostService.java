package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.PostRequestDto;
import com.kartik.blog_platform_api.dto.PostResponseDto;
import com.kartik.blog_platform_api.entity.Post;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.repository.PostRepository;
import com.kartik.blog_platform_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    public PostResponseDto createPost(PostRequestDto requestDto) {

        User author = userRepository.findById(requestDto.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Post post = new Post();
        post.setTitle(requestDto.title());
        post.setContent(requestDto.content());
        post.setAuthor(author);

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent(),
                getAuthorName(savedPost)
        );
    }


    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(post -> new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        getAuthorName(post)
                ))
                .toList();
    }

    public List<PostResponseDto> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(post -> new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent(),
                        getAuthorName(post)
                ))
                .toList();
    }


    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found"));

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                getAuthorName(post)
        );
    }


    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found"));

        User author = userRepository.findById(requestDto.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        existingPost.setTitle(requestDto.title());
        existingPost.setContent(requestDto.content());
        existingPost.setAuthor(author);

        Post updatedPost = postRepository.save(existingPost);

        return new PostResponseDto(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent(),
                getAuthorName(updatedPost)
        );
    }


    public void deletePost(Long id) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found"));

        postRepository.delete(existingPost);
    }


    private String getAuthorName(Post post) {
        if (post.getAuthor() == null) {
            return null;
        }

        return post.getAuthor().getName();
    }
}