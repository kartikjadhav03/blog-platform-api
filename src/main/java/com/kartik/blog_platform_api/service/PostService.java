package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.entity.Post;
import com.kartik.blog_platform_api.repository.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post createPost(Post post){
        return postRepository.save(post);
    }
    public List<Post> getAllPosts(){
        return postRepository.findAll();
    }

    public Optional<Post>getPostById(Long id){
        return postRepository.findById(id);
    }
    public Post updatePost(Long id,Post updatedPost){
        Post existingPost =postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Post not found"));

        existingPost.setTitle(updatedPost.getTitle());
        existingPost.setContent(updatedPost.getContent());

        return postRepository.save(existingPost);
    }
    public void deletePost(Long id) {
       Post existingPost = postRepository.findById(id)
               .orElseThrow(()->new ResponseStatusException(
                       HttpStatus.NOT_FOUND,"Post not found"));

       postRepository.delete(existingPost);
    }



}
