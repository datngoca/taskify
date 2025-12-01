package com.example.taskify_backend.exception;

public class NotFoundUserException extends AppException{
    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
