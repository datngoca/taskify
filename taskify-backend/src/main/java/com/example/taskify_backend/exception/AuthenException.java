package com.example.taskify_backend.exception;

public class AuthenException extends AppException {
    public AuthenException(ErrorCode errorCode) {
        super(errorCode);
    }
    
}
