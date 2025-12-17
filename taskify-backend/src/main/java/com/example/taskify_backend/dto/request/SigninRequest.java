package com.example.taskify_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SigninRequest(
    @NotBlank(message = "NOT_BLANK")
    String username,

    @NotBlank(message = "NOT_BLANK")
    @Size(min = 8, message = "INVALID_PASSWORD")
    String password
) {}