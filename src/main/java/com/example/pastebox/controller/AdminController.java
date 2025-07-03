package com.example.pastebox.controller;

import com.example.pastebox.dto.PasteboxResponseDto;
import com.example.pastebox.service.PasteboxService;
import com.example.pastebox.util.error.exception.InvalidPasteboxStatusException;
import com.example.pastebox.util.error.exception.NoSuchPasteboxException;
import com.example.pastebox.util.error.response.AppErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/admin")
public class AdminController {

    private final PasteboxService pasteboxService;

    @GetMapping("/index")
    public ResponseEntity<List<PasteboxResponseDto>> findAll(){
        return ResponseEntity.ok(pasteboxService.getAll());
    }
    @DeleteMapping("/{url}")
    public ResponseEntity<String> deletePastebox(@PathVariable String url){
        pasteboxService.deletePastebox(url);
        return ResponseEntity.ok("Pastebox {"+url+"} deleted successfully");
    }

    @ExceptionHandler(NoSuchPasteboxException.class)
    public ResponseEntity<AppErrorResponse> handleNoSuchPastepoxException(NoSuchPasteboxException
                                                                                      e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AppErrorResponse(e.getMessage()));
    }
}
