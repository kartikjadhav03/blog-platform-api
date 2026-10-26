package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.PostRequestDto;
import com.kartik.blog_platform_api.dto.PostResponseDto;
import com.kartik.blog_platform_api.entity.Post;
import com.kartik.blog_platform_api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;


@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = new Post();

        post.setTitle(requestDto.title());
        post.setContent(requestDto.content());

        Post savedPost = postRepository.save(post);

        return new PostResponseDto(
                savedPost.getId(),
                savedPost.getTitle(),
                savedPost.getContent()
        );
    }

    public List<PostResponseDto> getAllPosts(){
        return postRepository.findAll()
                .stream()
                .map(post -> new PostResponseDto(
                        post.getId(),
                        post.getTitle(),
                        post.getContent()
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
                post.getContent()
        );
    }
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found"));

        existingPost.setTitle(requestDto.title());
        existingPost.setContent(requestDto.content());

        Post updatedPost = postRepository.save(existingPost);

        return new PostResponseDto(
                updatedPost.getId(),
                updatedPost.getTitle(),
                updatedPost.getContent()
        );
    }
    public void deletePost(Long id) {
       Post existingPost = postRepository.findById(id)
               .orElseThrow(()->new ResponseStatusException(
                       HttpStatus.NOT_FOUND,"Post not found"));

       postRepository.delete(existingPost);
    }



}
