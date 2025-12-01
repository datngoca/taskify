package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.request.SigninRequest;
import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.dto.response.JwtResponse;
import com.example.taskify_backend.dto.response.UserResponse;
import com.example.taskify_backend.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    // 1. ĐĂNG KÝ

    @PostMapping("/signup")
    public ApiResponse<String> registerUser(@RequestBody SignupRequest signUpRequest) {
        String jwtResponse = authenticationService.register(signUpRequest);

        return ApiResponse.<String>builder()
                .code(201)
                .message("User registered successfully!")
                .result(jwtResponse)
                .build();
    }

    // 2. ĐĂNG NHẬP

    @PostMapping("/signin")
    public ApiResponse<JwtResponse> authenticateUser(@RequestBody SigninRequest loginRequest) {

        JwtResponse jwtResponse = authenticationService.login(loginRequest);
        // Trả về token cho người dùng
        return ApiResponse.<JwtResponse>builder()
                .code(200)
                .message("User signed in successfully!")
                .result(jwtResponse)
                .build();
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get current user successfully!")
                .result(authenticationService.getCurrentUser())
                .build();
    }
}