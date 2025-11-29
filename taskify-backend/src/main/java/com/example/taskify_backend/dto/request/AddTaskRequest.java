package com.example.taskify_backend.dto.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddTaskRequest {
    private String title;
    private String description;
    private String status;
}
