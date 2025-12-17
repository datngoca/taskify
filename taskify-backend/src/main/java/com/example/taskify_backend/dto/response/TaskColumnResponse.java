package com.example.taskify_backend.dto.response;

import java.util.List;

import lombok.Builder;

@Builder
public record TaskColumnResponse(
    Long id,
    String title,
    Long boardId,
    List<String> cardOrderIds,
    List<TaskResponse> tasks
) {
}
