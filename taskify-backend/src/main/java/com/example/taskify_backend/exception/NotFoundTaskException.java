package com.example.taskify_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundTaskException extends RuntimeException{
    public NotFoundTaskException(String message) {
        super(message);
    }
}
