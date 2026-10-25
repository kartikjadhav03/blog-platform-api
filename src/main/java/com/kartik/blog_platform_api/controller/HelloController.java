package com.kartik.blog_platform_api.controller;

import com.kartik.blog_platform_api.service.WelcomeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class HelloController {

    private final WelcomeService welcomeService;

    public HelloController(WelcomeService welcomeService) {
        this.welcomeService = welcomeService;
    }

    @GetMapping("/")
    public String home() {
        return welcomeService.getWelcomeMessage();
    }

}
