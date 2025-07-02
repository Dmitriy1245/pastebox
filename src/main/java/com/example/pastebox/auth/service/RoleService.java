package com.example.pastebox.auth.service;

import com.example.pastebox.auth.entity.Role;

import java.util.List;
import java.util.Optional;


public interface RoleService {
    Optional<Role> findByName(String name);

    Role getUserRole();
}
