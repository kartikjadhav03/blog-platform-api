package com.kartik.blog_platform_api.service;

import org.springframework.stereotype.Service;

@Service
public class WelcomeService {

    public String getWelcomeMessage() {
        return "Welcome to Blog Platform API!";
    }
}
