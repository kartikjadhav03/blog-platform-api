package com.kartik.blog_platform_api.service;

import com.kartik.blog_platform_api.dto.LoginRequestDto;
import com.kartik.blog_platform_api.dto.LoginResponseDto;
import com.kartik.blog_platform_api.dto.UserRequestDto;
import com.kartik.blog_platform_api.dto.UserResponseDto;
import com.kartik.blog_platform_api.entity.User;
import com.kartik.blog_platform_api.exception.EmailAlreadyExistsException;
import com.kartik.blog_platform_api.exception.InvalidCredentialsException;
import com.kartik.blog_platform_api.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }
    public UserResponseDto createUser(UserRequestDto requestDto) {
        if (userRepository.findByEmail(requestDto.email()).isPresent()) {
            throw new EmailAlreadyExistsException("Email already exists");
        }
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
    public LoginResponseDto login(LoginRequestDto requestDto) {

        User user = userRepository.findByEmail(requestDto.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(requestDto.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        UserResponseDto userResponse = new UserResponseDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );

        return new LoginResponseDto(token, userResponse);
    }
    }
