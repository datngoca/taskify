package com.example.taskify_backend.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTaskRequest {

    private String taskName;
    private String taskDescription;

}
