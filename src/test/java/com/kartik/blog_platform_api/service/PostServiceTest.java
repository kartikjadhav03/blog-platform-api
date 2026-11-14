
package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.PostRequestDto;
import com.kartik.blog_platform_api.dto.PostResponseDto;
import com.kartik.blog_platform_api.entity.Post;
import com.kartik.blog_platform_api.entity.Role;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.repository.PostRepository;
import com.kartik.blog_platform_api.repository.UserRepository;
import com.kartik.blog_platform_api.repository.CategoryRepository;
import com.kartik.blog_platform_api.repository.TagRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TagRepository tagRepository;

    @InjectMocks
    private PostService postService;




    private void setAuthenticatedUser(String email) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(email, null)
        );
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }



    @Test
    void getPostById_shouldReturnPost_whenPostExists() {

        Post post = new Post(
                "Best Food Places in Pune",
                "Explore popular restaurants."
        );
        post.setId(1L);

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        PostResponseDto response = postService.getPostById(1L);

        assertEquals("Best Food Places in Pune", response.title());
        assertEquals("Explore popular restaurants.", response.content());
    }



    @Test
    void getPostById_shouldThrowNotFound_whenPostDoesNotExist() {

        when(postRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.getPostById(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }



    @Test
    void searchPostsByTitle_shouldReturnMatchingPosts() {

        Post post = new Post(
                "Best Food Places in Pune",
                "Explore restaurants in Pune."
        );
        post.setId(1L);

        when(postRepository.findByTitleContainingIgnoreCase("Pune"))
                .thenReturn(List.of(post));

        List<PostResponseDto> response =
                postService.searchPostsByTitle("Pune");

        assertEquals(1, response.size());
        assertEquals("Best Food Places in Pune", response.get(0).title());
    }


    @Test
    void searchPostsByTitle_shouldReturnEmptyList_whenNoPostMatches() {

        when(postRepository.findByTitleContainingIgnoreCase("Unknown"))
                .thenReturn(List.of());

        List<PostResponseDto> response =
                postService.searchPostsByTitle("Unknown");

        assertEquals(0, response.size());
    }


    @Test
    void getAllPosts_shouldReturnPostsInDescendingOrder() {

        Post post1 = new Post(
                "Best Food Places in Pune",
                "Explore restaurants."
        );
        post1.setId(1L);

        Post post2 = new Post(
                "Beautiful Beaches in Goa",
                "Explore Goa beaches."
        );
        post2.setId(2L);

        Pageable pageable = PageRequest.of(0, 10);

        Pageable sortedPageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        Page<Post> postPage = new PageImpl<>(
                List.of(post1, post2)
        );

        when(postRepository.findAll(sortedPageable))
                .thenReturn(postPage);

        Page<PostResponseDto> response =
                postService.getAllPosts(pageable, "newest");

        assertEquals(2, response.getTotalElements());
        assertEquals(
                "Best Food Places in Pune",
                response.getContent().get(0).title()
        );
        assertEquals(
                "Beautiful Beaches in Goa",
                response.getContent().get(1).title()
        );
    }


    @Test
    void searchAndFilterPosts_shouldReturnMatchingPosts() {

        Post post = new Post(
                "Best Food Places in Pune",
                "Explore popular restaurants in Pune."
        );
        post.setId(1L);

        Pageable pageable = PageRequest.of(0, 10);

        Pageable sortedPageable = PageRequest.of(
                0,
                10,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        when(categoryRepository.existsById(1L))
                .thenReturn(true);

        when(tagRepository.existsById(2L))
                .thenReturn(true);

        Page<Post> postPage = new PageImpl<>(
                List.of(post)
        );

        when(postRepository.searchAndFilterPosts(
                "Pune",
                1L,
                2L,
                sortedPageable
        )).thenReturn(postPage);

        Page<PostResponseDto> response =
                postService.searchAndFilterPosts(
                        "Pune",
                        1L,
                        2L,
                        pageable,
                        "newest"
                );

        assertEquals(1, response.getTotalElements());
        assertEquals(
                "Best Food Places in Pune",
                response.getContent().get(0).title()
        );
    }


    @Test
    void searchAndFilterPosts_shouldThrowNotFound_whenCategoryDoesNotExist() {

        when(categoryRepository.existsById(99L))
                .thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.searchAndFilterPosts(
                        "Pune",
                        99L,
                        null,
                        PageRequest.of(0, 10),
                        "newest"
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(postRepository, never())
                .searchAndFilterPosts(any(), any(), any(), any());
    }


    @Test
    void searchAndFilterPosts_shouldThrowNotFound_whenTagDoesNotExist() {

        when(categoryRepository.existsById(1L))
                .thenReturn(true);

        when(tagRepository.existsById(99L))
                .thenReturn(false);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.searchAndFilterPosts(
                        "Pune",
                        1L,
                        99L,
                        PageRequest.of(0, 10),
                        "newest"
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(postRepository, never())
                .searchAndFilterPosts(any(), any(), any(), any());
    }


    @Test
    void updatePost_shouldUpdatePost_whenAdminUpdates() {

        Post post = new Post("Old Title", "Old Content");
        post.setId(1L);

        User admin = mock(User.class);

        when(admin.getRole()).thenReturn(Role.ADMIN);

        setAuthenticatedUser("admin@example.com");

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(admin));

        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        PostRequestDto request = new PostRequestDto(
                "Updated Travel Places",
                "Explore beautiful places in India.",
                null,
                null
        );

        PostResponseDto response =
                postService.updatePost(1L, request);

        assertEquals("Updated Travel Places", response.title());
        assertEquals(
                "Explore beautiful places in India.",
                response.content()
        );
    }


    @Test
    void updatePost_shouldThrowNotFound_whenPostDoesNotExist() {

        when(postRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.updatePost(
                        99L,
                        new PostRequestDto(
                                "Title",
                                "Content",
                                null,
                                null
                        )
                )
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(postRepository, never()).save(any(Post.class));
    }


    @Test
    void updatePost_shouldThrowForbidden_whenAnotherUserUpdatesPost() {

        Post post = new Post("Old Title", "Old Content");
        post.setId(1L);

        User author = mock(User.class);
        when(author.getId()).thenReturn(10L);
        post.setAuthor(author);

        User loggedInUser = mock(User.class);

        when(loggedInUser.getId()).thenReturn(20L);
        when(loggedInUser.getRole()).thenReturn(Role.USER);

        setAuthenticatedUser("user@example.com");

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(loggedInUser));

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.updatePost(
                        1L,
                        new PostRequestDto(
                                "Updated Title",
                                "Updated Content",
                                null,
                                null
                        )
                )
        );

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatusCode());

        verify(postRepository, never()).save(any(Post.class));
    }



    @Test
    void deletePost_shouldDeletePost_whenAdminDeletes() {

        Post post = new Post("Travel Guide", "Explore India.");
        post.setId(1L);

        User admin = mock(User.class);
        when(admin.getRole()).thenReturn(Role.ADMIN);

        setAuthenticatedUser("admin@example.com");

        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));

        when(userRepository.findByEmail("admin@example.com"))
                .thenReturn(Optional.of(admin));

        postService.deletePost(1L);

        verify(postRepository).delete(post);
    }


    @Test
    void deletePost_shouldThrowNotFound_whenPostDoesNotExist() {

        when(postRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.deletePost(99L)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());

        verify(postRepository, never()).delete(any(Post.class));
    }

    @Test
    void createPost_shouldCreatePostSuccessfully() {

        User user = mock(User.class);
        when(user.getName()).thenReturn("Test User");

        setAuthenticatedUser("user@example.com");

        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(user));

        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> {
                    Post post = invocation.getArgument(0);
                    post.setId(1L);
                    return post;
                });

        PostRequestDto request = new PostRequestDto(
                "Beautiful Places in Goa",
                "Explore beautiful beaches in Goa.",
                null,
                null
        );

        PostResponseDto response = postService.createPost(request);

        assertEquals("Beautiful Places in Goa", response.title());
        assertEquals("Explore beautiful beaches in Goa.", response.content());
        assertEquals("Test User", response.authorName());
    }

    @Test
    void createPost_shouldThrowNotFound_whenCategoryDoesNotExist() {

        User user = org.mockito.Mockito.mock(User.class);

        setAuthenticatedUser("user@example.com");

        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(user));

        when(categoryRepository.findById(99L))
                .thenReturn(Optional.empty());

        PostRequestDto request = new PostRequestDto(
                "Java Learning",
                "Learning Spring Boot",
                99L,
                null
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.createPost(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    void createPost_shouldThrowNotFound_whenTagsDoNotExist() {

        User user = org.mockito.Mockito.mock(User.class);

        setAuthenticatedUser("user@example.com");

        when(userRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(user));

        when(tagRepository.findAllById(Set.of(1L, 2L)))
                .thenReturn(List.of());

        PostRequestDto request = new PostRequestDto(
                "Java Learning",
                "Learning Spring Boot",
                null,
                Set.of(1L, 2L)
        );

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> postService.createPost(request)
        );

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(postRepository, never()).save(any(Post.class));



    }
}
