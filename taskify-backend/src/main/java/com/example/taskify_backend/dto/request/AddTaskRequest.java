package com.example.taskify_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddTaskRequest {
    @NotBlank(message = "NOT_BLANK")
    private String title;
    // @NotBlank(message = "NOT_BLANK")
    private String description;
    @NotBlank(message = "NOT_BLANK")
    private String status;
}
