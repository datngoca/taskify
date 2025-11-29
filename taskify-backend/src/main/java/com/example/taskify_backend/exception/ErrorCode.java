package com.example.taskify_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum ErrorCode {
    TASK_EXISTED(101, "Task Existed!"),
    TASK_NOTFOUND(404, "Task Not Found!"),
;
    private final int code;
    private final String message;
}


