package com.kartik.blog_platform_api.repository;

import com.kartik.blog_platform_api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}