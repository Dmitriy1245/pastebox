package com.example.pastebox.dto;

import com.example.pastebox.entity.PasteboxStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PasteboxResponseDto {

    private String data;
    private PasteboxStatus pasteboxStatus; //PUBLIC, UNLISTED
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt; //Если null, то бессрочно
    private String hash;
    private String author;
}
