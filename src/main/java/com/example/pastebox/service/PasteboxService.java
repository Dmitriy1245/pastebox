package com.example.pastebox.service;

import com.example.pastebox.dto.PasteboxRequestDto;
import com.example.pastebox.dto.PasteboxResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PasteboxService {
    PasteboxResponseDto createPastebox(PasteboxRequestDto dto);

    PasteboxResponseDto getForUrl(String url);

    List<PasteboxResponseDto> getAllPublic();

    List<PasteboxResponseDto> getAll();

    Page<PasteboxResponseDto> getAllPublic(Pageable pageable);

    List<PasteboxResponseDto> containsDmitriy();
}