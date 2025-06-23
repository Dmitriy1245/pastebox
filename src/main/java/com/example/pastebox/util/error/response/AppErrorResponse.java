package com.example.pastebox.util.error.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppErrorResponse {
    private String msg;
    private LocalDateTime timestamp;
    public AppErrorResponse(String message) {
        this.msg = message;
        this.timestamp = LocalDateTime.now();
    }
}
