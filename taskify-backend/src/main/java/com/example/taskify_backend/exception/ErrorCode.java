package com.example.taskify_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1001, "Uncategorized error", HttpStatus.BAD_REQUEST),

    // User
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1005, "User not found", HttpStatus.NOT_FOUND),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(1003, "Username must be at least 4 characters", HttpStatus.BAD_REQUEST),
    NOT_BLANK(1006, "Field cannot be blank", HttpStatus.BAD_REQUEST),

    // Task
    TASK_NOT_FOUND(2001, "Task not found", HttpStatus.NOT_FOUND),
    TASK_EXISTED(2002, "Task Existed!", HttpStatus.BAD_REQUEST),

    // Auth & Permission
    AUTHENTICATION_FAILED(3001, "Authentication Failed!", HttpStatus.UNAUTHORIZED),
    PERMISSION_DENIED(3002, "Permission Denied!", HttpStatus.FORBIDDEN),
    INVALID_TOKEN(3003, "Invalid Token!", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(3004, "Invalid Refresh Token!", HttpStatus.UNAUTHORIZED),
    TOKEN_EXPIRED(3005, "Expired refresh token", HttpStatus.UNAUTHORIZED);

    private final int code;
    private final String message;
    private final HttpStatus statusCode;
}
