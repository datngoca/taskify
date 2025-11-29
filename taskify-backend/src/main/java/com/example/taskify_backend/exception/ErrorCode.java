package com.example.taskify_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    TASK_EXISTED(101, "Task Existed!"),
    TASK_NOT_FOUND(404, "Task Not Found!"),
    TITLE_NOT_FOUND(123, "Title Not Found!"),
    DESCRIPTION_NOT_FOUND(122, "Description Not Found!"),
;
    private final int code;
    private final String message;
}


