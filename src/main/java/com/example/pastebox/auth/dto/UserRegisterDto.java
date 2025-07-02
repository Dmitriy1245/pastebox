package com.example.pastebox.auth.dto;

import com.example.pastebox.auth.entity.Role;
import com.example.pastebox.entity.Pastebox;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UserRegisterDto {

    private String username;
    private String password;
    private String email;

}
