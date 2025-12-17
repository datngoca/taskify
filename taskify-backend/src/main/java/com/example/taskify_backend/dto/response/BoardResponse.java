package com.example.taskify_backend.dto.response;

import java.util.List;

public record BoardResponse(
    Long id,
    String title,
    List<String> columnOrderIds,
    List<TaskColumnResponse> columns
) {
}
