package com.example.taskify_backend.dto.response;

import lombok.*;

@AllArgsConstructor
@Getter
public class JwtResponse {
    private String token;
    private String username;
}
