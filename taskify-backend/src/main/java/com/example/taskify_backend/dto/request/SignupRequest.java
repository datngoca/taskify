package com.example.taskify_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank(message = "NOT_BLANK")
    @Size(min = 4, message = "INVALID_USERNAME")
    String username,

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password,
    
    String role
) {}
