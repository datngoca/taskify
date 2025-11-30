package com.example.taskify_backend.exception;

public class AlreadyExistUserException extends AppException {
    public AlreadyExistUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
