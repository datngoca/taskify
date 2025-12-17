package com.example.taskify_backend.dto.response;

public record TaskResponse(
        Long id,
        String title,
        Long columnId) {
}