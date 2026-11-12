
package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.PostRequestDto;
import com.kartik.blog_platform_api.dto.PostResponseDto;
import com.kartik.blog_platform_api.entity.Post;
import com.kartik.blog_platform_api.entity.Role;
import com.kartik.blog_platform_api.entity.Tag;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.repository.CategoryRepository;
import com.kartik.blog_platform_api.repository.PostRepository;
import com.kartik.blog_platform_api.repository.TagRepository;
import com.kartik.blog_platform_api.repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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
            TagRepository tagRepository
    ) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }


    private User getLoggedInUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));
    }


    private void checkPostOwnership(Post post) {

        User loggedInUser = getLoggedInUser();


        if (loggedInUser.getRole() == Role.ADMIN) {
            return;
        }


        if (post.getAuthor() == null) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "This post has no author assigned"
            );
        }


        if (!post.getAuthor().getId().equals(loggedInUser.getId())) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to modify this post"
            );
        }
    }


    public PostResponseDto createPost(PostRequestDto requestDto) {


        User author = getLoggedInUser();

        Post post = new Post();

        post.setTitle(requestDto.title());
        post.setContent(requestDto.content());
        post.setAuthor(author);

        post.setCategory(
                requestDto.categoryId() == null
                        ? null
                        : categoryRepository.findById(requestDto.categoryId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Category not found"
                        ))
        );

        List<Tag> tags = requestDto.tagIds() == null
                ? List.of()
                : tagRepository.findAllById(requestDto.tagIds());

        if (requestDto.tagIds() != null
                && tags.size() != requestDto.tagIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "One or more tags not found"
            );
        }

        post.setTags(new HashSet<>(tags));

        Post savedPost = postRepository.save(post);

        return mapToPostResponse(savedPost);
    }


    public Page<PostResponseDto> getAllPosts(
            Pageable pageable,
            String sort
    ) {

        Sort.Direction direction = "oldest".equalsIgnoreCase(sort)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(direction, "createdAt")
        );

        return postRepository.findAll(sortedPageable)
                .map(this::mapToPostResponse);
    }


    public Page<PostResponseDto> searchAndFilterPosts(
            String title,
            Long categoryId,
            Long tagId,
            Pageable pageable,
            String sort
    ) {

        if (categoryId != null
                && !categoryRepository.existsById(categoryId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Category not found"
            );
        }

        if (tagId != null
                && !tagRepository.existsById(tagId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Tag not found"
            );
        }

        Sort.Direction direction = "oldest".equalsIgnoreCase(sort)
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(direction, "createdAt")
        );

        return postRepository.searchAndFilterPosts(
                        title,
                        categoryId,
                        tagId,
                        sortedPageable
                )
                .map(this::mapToPostResponse);
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
                        HttpStatus.NOT_FOUND,
                        "Post not found"
                ));

        return mapToPostResponse(post);
    }


    public PostResponseDto updatePost(
            Long id,
            PostRequestDto requestDto
    ) {

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post not found"
                ));


        checkPostOwnership(existingPost);

        existingPost.setTitle(requestDto.title());
        existingPost.setContent(requestDto.content());

        existingPost.setCategory(
                requestDto.categoryId() == null
                        ? null
                        : categoryRepository.findById(requestDto.categoryId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Category not found"
                        ))
        );

        List<Tag> tags = requestDto.tagIds() == null
                ? List.of()
                : tagRepository.findAllById(requestDto.tagIds());

        if (requestDto.tagIds() != null
                && tags.size() != requestDto.tagIds().size()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "One or more tags not found"
            );
        }

        existingPost.setTags(new HashSet<>(tags));

        Post updatedPost = postRepository.save(existingPost);

        return mapToPostResponse(updatedPost);
    }


    public void deletePost(Long id) {

        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Post not found"
                ));

        // Only the post owner can delete it
        checkPostOwnership(existingPost);

        postRepository.delete(existingPost);
    }


    private PostResponseDto mapToPostResponse(Post post) {

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),

                post.getAuthor() == null
                        ? null
                        : post.getAuthor().getName(),

                post.getCategory() == null
                        ? null
                        : post.getCategory().getName(),

                post.getTags()
                        .stream()
                        .map(Tag::getName)
                        .toList(),

                post.getStatus(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }
}
