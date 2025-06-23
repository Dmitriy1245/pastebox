package com.example.pastebox.dto;

import lombok.Data;

@Data
public class PasteboxRequestDto {

    private String data;
    private String pasteboxStatus; //PUBLIC, UNLISTED
    private String lifetime; //10m, 1h,3h,1d,1w,1mo or null
}
