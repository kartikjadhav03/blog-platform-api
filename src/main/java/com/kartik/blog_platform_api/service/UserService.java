package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.UserRequestDto;
import com.kartik.blog_platform_api.dto.UserResponseDto;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public UserResponseDto createUser(UserRequestDto requestDto) {
        User user = new User(
                requestDto.name(),
                requestDto.email()
        );

        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
}