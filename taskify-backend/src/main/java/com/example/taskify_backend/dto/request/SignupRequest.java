package com.example.taskify_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
public class SignupRequest {
    @NotBlank(message = "NOT_BLANK")
    @Size(min = 4, message = "INVALID_USERNAME")
    private String username;
    @NotBlank(message = "NOT_BLANK")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
    private String role;
}