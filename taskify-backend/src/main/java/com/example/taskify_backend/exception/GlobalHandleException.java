package com.example.taskify_backend.exception;

import com.example.taskify_backend.dto.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalHandleException {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ApiResponse<String>> handleUncategorizedException(Exception exception) {
        log.error("Uncategorized exception: ", exception);
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED_EXCEPTION.getStatusCode())
                .body(ApiResponse.<String>builder()
                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                        .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                        .build());
    }

    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<String>> handleRuntimeException(RuntimeException ex) {
        log.error("Runtime exception: ", ex);
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.<String>builder()
                        .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse<Void>> handleAppException(AppException errorCode) {
        return ResponseEntity
                .status(errorCode.getErrorCode().getStatusCode())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getErrorCode().getCode())
                        .message(errorCode.getErrorCode().getMessage())
                        .build());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try {
            errorCode = ErrorCode.valueOf(enumKey);
        } catch (IllegalArgumentException ex) {
            log.error("Invalid ErrorCode key: {}", enumKey);
        }

        return ResponseEntity
                .status(errorCode.getStatusCode())
                .body(ApiResponse.<Void>builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}
