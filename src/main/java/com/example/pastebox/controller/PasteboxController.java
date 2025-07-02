package com.example.pastebox.controller;


import com.example.pastebox.dto.PasteboxRequestDto;
import com.example.pastebox.dto.PasteboxResponseDto;
import com.example.pastebox.service.PasteboxService;
import com.example.pastebox.util.error.exception.ExpiredPasteboxException;
import com.example.pastebox.util.error.exception.InvalidPasteboxStatusException;
import com.example.pastebox.util.error.exception.NoSuchPasteboxException;
import com.example.pastebox.util.error.response.AppErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PasteboxController {

    private final PasteboxService pasteboxService;

    @PostMapping("/add")
    public ResponseEntity<PasteboxResponseDto> addPastebox(@RequestBody PasteboxRequestDto pasteboxRequestDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(pasteboxService.createPastebox(pasteboxRequestDto));
    }

    @GetMapping("/{url}")
    public ResponseEntity<PasteboxResponseDto> showPastebox(@PathVariable String url){
        return ResponseEntity.ok(pasteboxService.getForUrl(url));
    }

    @GetMapping("/page")
    public ResponseEntity<Page<PasteboxResponseDto>> getPasteboxPage(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        return ResponseEntity.ok(pasteboxService.getAllPublic(pageable));
    }

    @GetMapping("/admin/index")
    public ResponseEntity<List<PasteboxResponseDto>> findAll(){
        return ResponseEntity.ok(pasteboxService.getAll());
    }

    @ExceptionHandler({InvalidPasteboxStatusException.class, NoSuchPasteboxException.class})
    public ResponseEntity<AppErrorResponse> handleInvalidPastebocStatus(Exception e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppErrorResponse(e.getMessage()));
    }

    @ExceptionHandler(ExpiredPasteboxException.class)
    public ResponseEntity<AppErrorResponse> handleExpiredPastebox(ExpiredPasteboxException e){
        return ResponseEntity.status(HttpStatus.GONE).body(new AppErrorResponse(e.getMessage()));
    }

}
