package com.example.taskify_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),

    // Auth & Permission
    AUTHENTICATION_FAILED(1002, "Authentication Failed!", HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED(1003, "Permission Denied!", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(1004, "Invalid Token!", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(1005, "Invalid Refresh Token!", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(1006, "Expired Token", HttpStatus.UNAUTHORIZED),

    // User
    USER_EXISTED(2001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(2002, "User not found", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(2003, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(2004, "Username must be at least 4 characters", HttpStatus.BAD_REQUEST),
    NOT_BLANK(2005, "Field cannot be blank", HttpStatus.BAD_REQUEST),

    // Task
    TASK_NOT_FOUND(3001, "Task not found", HttpStatus.NOT_FOUND),
    TASK_EXISTED(3002, "Task Existed!", HttpStatus.BAD_REQUEST),

    // TaskColumn
    TASK_COLUMN_NOT_FOUND(4001, "Task Column not found",HttpStatus.NOT_FOUND),

    // Board
    BOARD_NOT_FOUND(5001, "Board not found", HttpStatus.NOT_FOUND),

    ;

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
