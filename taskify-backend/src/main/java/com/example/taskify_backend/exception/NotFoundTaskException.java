package com.example.taskify_backend.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotFoundTaskException extends RuntimeException{

    private ErrorCode errorCode;

    public NotFoundTaskException(ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

}
