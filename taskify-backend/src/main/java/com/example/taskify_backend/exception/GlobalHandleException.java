package com.example.taskify_backend.exception;

import com.example.taskify_backend.dto.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRunTimeException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.<Void>builder()
                        .code(1111)
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
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return ResponseEntity
                .badRequest()
                .body(ApiResponse.builder()
                        .code(400)
                        .message(ErrorCode
                                .valueOf(e.getFieldError().getDefaultMessage())
                                .getMessage())
                        .build());
    }


}
