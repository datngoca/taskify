package com.example.taskify_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    TASK_EXISTED(101, "Task Existed!", HttpStatus.BAD_REQUEST),
    TASK_NOT_FOUND(404, "Task Not Found!", HttpStatus.NOT_FOUND),
    TITLE_NOT_FOUND(123, "Title Not Found!", HttpStatus.NOT_FOUND),
    DESCRIPTION_NOT_FOUND(122, "Description Not Found!", HttpStatus.NOT_FOUND),
    PERMISSION_DENIED(403, "Permission Denied!", HttpStatus.FORBIDDEN)
;
    private final int code;
    private final String message;
    private final HttpStatus status;
}


