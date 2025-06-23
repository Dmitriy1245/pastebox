package com.example.pastebox.util.error.exception;

public class ExpiredPasteboxException extends RuntimeException {
    public ExpiredPasteboxException(String message) {
        super(message);
    }
}
