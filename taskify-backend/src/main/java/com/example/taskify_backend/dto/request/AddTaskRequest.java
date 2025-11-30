package com.example.taskify_backend.dto.request;

import com.example.taskify_backend.entity.User;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AddTaskRequest {
    private String title;
    private String description;
    private String status;
    private User user;
}
