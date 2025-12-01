package com.example.taskify_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {
    private String username;
    @NotBlank(message = "NOT_BLANK")
    @Size(min = 8, message = "INVALID_PASSWORD")
    private String password;
}
