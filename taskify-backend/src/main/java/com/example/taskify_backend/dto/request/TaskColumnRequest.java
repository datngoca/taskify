package com.example.taskify_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TaskColumnRequest(
    @NotBlank(message = "Title is required")
    String title,
        
    @NotNull(message = "Board ID is required")
    Long boardId
) {
}
