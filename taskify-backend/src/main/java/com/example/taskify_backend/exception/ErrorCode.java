package com.example.taskify_backend.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    TASK_EXISTED(101, "Task Existed!", HttpStatus.BAD_REQUEST),
    USER_EXISTED(102, "User Existed!", HttpStatus.BAD_REQUEST),

    TASK_NOT_FOUND(404, "Task Not Found!", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(404, "User Not Found!", HttpStatus.NOT_FOUND),

    PERMISSION_DENIED(999, "Permission Denied!", HttpStatus.FORBIDDEN),

    INVALID_PASSWORD(301, "Password must be at least 8 characters long", HttpStatus.BAD_REQUEST),
    INVALID_USERNAME(302, "Username must be at least 4 characters long", HttpStatus.BAD_REQUEST),
    NOT_BLANK(303, "Field cannot be blank", HttpStatus.BAD_REQUEST),

    AUTHENTICATION_FAILED(401, "Authentication Failed!", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(402, "Invalid Token!", HttpStatus.UNAUTHORIZED),
    INVALID_REFRESH_TOKEN(403, "Invalid Refresh Token!", HttpStatus.UNAUTHORIZED)

    ;

    private final int code;
    private final String message;
    private HttpStatus statusCode;
}
