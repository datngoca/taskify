package com.example.taskify_backend.dto.request;

// DTO Request (Input)
public record TaskRequest(
        String title,
        Integer position,
        Long columnId) {
}