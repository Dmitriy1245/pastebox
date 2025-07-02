package com.example.pastebox.auth.service;

import com.example.pastebox.auth.entity.User;

import java.util.Optional;

public interface UserService{
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String username);
    User save(User user);
}
