package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.PostRequestDto;
import com.kartik.blog_platform_api.dto.PostResponseDto;
import com.kartik.blog_platform_api.entity.Post;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.repository.CategoryRepository;
import com.kartik.blog_platform_api.repository.PostRepository;
import com.kartik.blog_platform_api.repository.TagRepository;
import com.kartik.blog_platform_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PostService(
            PostRepository postRepository,
            UserRepository userRepository,
            CategoryRepository categoryRepository,
            TagRepository tagRepository) {

        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public PostResponseDto createPost(PostRequestDto requestDto) {

        User author = userRepository.findById(requestDto.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        Post post = new Post();
        post.setTitle(requestDto.title());
        post.setContent(requestDto.content());
        post.setAuthor(author);

        if (requestDto.categoryId() != null) {
            var category = categoryRepository.findById(requestDto.categoryId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Category not found"));

            post.setCategory(category);
        }

        if (requestDto.tagIds() != null && !requestDto.tagIds().isEmpty()) {
            var tags = tagRepository.findAllById(requestDto.tagIds());

            if (tags.size() != requestDto.tagIds().size()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "One or more tags not found");
            }

            post.setTags(new HashSet<>(tags));
        }

        Post savedPost = postRepository.save(post);

        return mapToPostResponse(savedPost);
    }

    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapToPostResponse)
                .toList();
    }

    public List<PostResponseDto> searchPostsByTitle(String title) {
        return postRepository.findByTitleContainingIgnoreCase(title)
                .stream()
                .map(this::mapToPostResponse)
                .toList();
    }

    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found"));

        return mapToPostResponse(post);
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

        existingPost.setCategory(
                requestDto.categoryId() == null
                        ? null
                        : categoryRepository.findById(requestDto.categoryId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND, "Category not found"))
        );

        var tags = requestDto.tagIds() == null
                ? List.<com.kartik.blog_platform_api.entity.Tag>of()
                : tagRepository.findAllById(requestDto.tagIds()).stream().toList();

        if (requestDto.tagIds() != null
                && tags.size() != requestDto.tagIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "One or more tags not found");
        }

        existingPost.setTags(new HashSet<>(tags));

        Post updatedPost = postRepository.save(existingPost);

        return mapToPostResponse(updatedPost);
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

    private PostResponseDto mapToPostResponse(Post post) {

        String categoryName = post.getCategory() != null
                ? post.getCategory().getName()
                : null;

        List<String> tagNames = post.getTags()
                .stream()
                .map(tag -> tag.getName())
                .toList();

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                getAuthorName(post),
                categoryName,
                tagNames,
                post.getStatus(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}