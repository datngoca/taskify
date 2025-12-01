package com.example.taskify_backend.exception;

public class InvalidUserException extends AppException{
    public InvalidUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
