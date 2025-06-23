package com.example.pastebox.util.mapper;

import com.example.pastebox.dto.PasteboxRequestDto;
import com.example.pastebox.dto.PasteboxResponseDto;
import com.example.pastebox.entity.Pastebox;
import com.example.pastebox.entity.PasteboxStatus;
import com.example.pastebox.util.error.exception.InvalidPasteboxStatusException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class EntityMapper {

    public Pastebox mapPasteboxDtoToPastebox(PasteboxRequestDto dto) {
        if(!isValidPasteboxStatus(dto.getPasteboxStatus())) throw new InvalidPasteboxStatusException(
                "Pastebox status "+dto.getPasteboxStatus()+" is invalid");
        Pastebox pastebox = new Pastebox();
        pastebox.setData(dto.getData());
        pastebox.setPasteboxStatus(PasteboxStatus.valueOf(dto.getPasteboxStatus().toUpperCase()));
        pastebox.setCreatedAt(LocalDateTime.now());
        if(dto.getLifetime()!=null) {
            pastebox.setExpiresAt(pastebox.getCreatedAt().plus(parseExpiration(dto.getLifetime())));
        }
        return pastebox;
    }

    private Duration parseExpiration(String input) {
        if(input==null) return null;
        return switch (input) {
            case "10m" -> Duration.ofMinutes(10);
            case "1h" -> Duration.ofHours(1);
            case "3h" -> Duration.ofHours(3);
            case "1d" -> Duration.ofDays(1);
            case "1w" -> Duration.ofDays(7);
            case "1mo" -> Duration.ofDays(30);
            default -> throw new IllegalArgumentException("Invalid expiration");
        };
    }


    public PasteboxResponseDto mapPasteboxToPasteboxResponseDto(Pastebox pastebox) {

        PasteboxResponseDto pasteboxResponseDto = new PasteboxResponseDto();
        pasteboxResponseDto.setData(pastebox.getData());
        pasteboxResponseDto.setPasteboxStatus(pastebox.getPasteboxStatus());
        pasteboxResponseDto.setCreatedAt(pastebox.getCreatedAt());
        pasteboxResponseDto.setExpiresAt(pastebox.getExpiresAt());
        pasteboxResponseDto.setHash(pastebox.getHash());
        return pasteboxResponseDto;
    }
    private boolean isValidPasteboxStatus(String status){
        try {
            PasteboxStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
