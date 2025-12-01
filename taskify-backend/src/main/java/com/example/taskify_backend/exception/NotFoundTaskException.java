package com.example.taskify_backend.exception;

public class NotFoundTaskException extends AppException {
    public NotFoundTaskException(ErrorCode errorCode) {
        super(errorCode);

    }
}
