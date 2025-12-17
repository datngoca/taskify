package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.request.SigninRequest;
import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.dto.response.JwtResponse;
import com.example.taskify_backend.dto.response.SigninResponse;
import com.example.taskify_backend.dto.response.UserResponse;
import com.example.taskify_backend.service.AuthenticationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ApiResponse<String> registerUser(@RequestBody SignupRequest signUpRequest) {
        String result = authenticationService.register(signUpRequest);
        return ApiResponse.<String>builder()
                .code(201)
                .message(result)
                .build();
    }

    @PostMapping("/signin")
    public ResponseEntity<ApiResponse<SigninResponse>> authenticateUser(@RequestBody SigninRequest loginRequest) {
        JwtResponse loginResult = authenticationService.login(loginRequest);

        ResponseCookie jwtCookie = ResponseCookie.from("refresh_token", loginResult.getRefreshToken())
                .httpOnly(true)
                .secure(false) // TODO: Configure for Production
                .path("/api/v1/auth/refresh-token")
                .maxAge(24 * 60 * 60)
                .sameSite("Strict")
                .build();

        SigninResponse signinResponse = new SigninResponse(loginResult.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(ApiResponse.<SigninResponse>builder()
                        .code(200)
                        .message("User signed in successfully!")
                        .result(signinResponse)
                        .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logoutUser() {
        authenticationService.logout();

        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth/refresh-token")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.<String>builder()
                        .code(200)
                        .message("Logout Successful")
                        .build());
    }

    @GetMapping("/me")
    public ApiResponse<UserResponse> getCurrentUser() {
        return ApiResponse.<UserResponse>builder()
                .code(200)
                .message("Get current user successfully!")
                .result(authenticationService.getCurrentUser())
                .build();
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<SigninResponse>> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String requestRefreshToken) {
        
        if (requestRefreshToken == null) {
             return ResponseEntity.badRequest().body(ApiResponse.<SigninResponse>builder()
                    .code(400)
                    .message("Refresh Token is missing")
                    .build());
        }

        JwtResponse jwtResponse = authenticationService.refreshToken(requestRefreshToken);

        ResponseCookie cookie = ResponseCookie.from("refresh_token", jwtResponse.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/api/v1/auth/refresh-token")
                .maxAge(24 * 60 * 60)
                .build();

        SigninResponse signinResponse = new SigninResponse(jwtResponse.getToken());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.<SigninResponse>builder()
                        .code(200)
                        .message("Token refreshed successfully!")
                        .result(signinResponse)
                        .build());
    }
}
