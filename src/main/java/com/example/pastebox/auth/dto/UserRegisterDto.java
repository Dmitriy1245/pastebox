package com.example.pastebox.auth.dto;

import lombok.Data;

@Data
public class UserRegisterDto {

    private String username;
    private String password;
    private String email;

}
