package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.dto.request.LoginRequest;
import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.response.JwtResponse;
import com.example.taskify_backend.exception.AlreadyExistUserException;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.repository.UserRepository;
import com.example.taskify_backend.security.jwt.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    // 1. ĐĂNG KÝ

    @PostMapping("/signup")
    public ApiResponse<?> registerUser(@RequestBody @Valid SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new AlreadyExistUserException(ErrorCode.USER_EXISTED);
        }

        // Tạo user mới (Mã hoá password)
        User user = new User(
                signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                "USER"
        );

        userRepository.save(user);

        return ApiResponse.builder().code(200).message("User registered successfully!").build();
    }

    // 2. ĐĂNG NHẬP

    @PostMapping("/signin")
    public ApiResponse<?> authenticateUser(@RequestBody @Valid LoginRequest loginRequest) {

        // Xác thực username/password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        // Nếu xác thực thành công, lưu vào context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Sinh ra token
        String jwt = jwtUtils.generateJwtToken(authentication);

        // Trả về token cho người dùng
        return ApiResponse.builder().code(200).message("User signed up successfully !").result(new JwtResponse(jwt, loginRequest.getUsername())).build();
    }
}