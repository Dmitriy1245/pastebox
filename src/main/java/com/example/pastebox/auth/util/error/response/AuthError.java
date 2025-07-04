package com.example.pastebox.auth.util.error.response;

import lombok.Data;

import java.util.Date;

@Data
public class AuthError {
    private String message;
    private Date timestamp;

    public AuthError(String message) {
        this.message = message;
        this.timestamp = new Date();
    }
}
