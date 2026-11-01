package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.LoginRequestDto;
import com.kartik.blog_platform_api.dto.UserRequestDto;
import com.kartik.blog_platform_api.dto.UserResponseDto;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public UserResponseDto createUser(UserRequestDto requestDto) {
        String hashedPassword = passwordEncoder.encode(requestDto.password());

        User user = new User(
                requestDto.name(),
                requestDto.email(),
                hashedPassword
        );

        User savedUser = userRepository.save(user);

        return new UserResponseDto(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail()
        );
    }
    public UserResponseDto login(LoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}