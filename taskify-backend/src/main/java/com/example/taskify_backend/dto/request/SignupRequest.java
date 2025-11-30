package com.example.taskify_backend.dto.request;

import lombok.*;

@Getter
@Setter
public class SignupRequest {
    private String username;
    private String password;
}