package com.example.taskify_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddTaskRequest {

    private String taskName;
    private String taskDescription;

}
