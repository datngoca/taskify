package com.example.taskify_backend.dto.response;

import lombok.*;

@AllArgsConstructor
@Getter
@ToString
public class JwtResponse {
    private String token;
    private String refreshToken;
}
