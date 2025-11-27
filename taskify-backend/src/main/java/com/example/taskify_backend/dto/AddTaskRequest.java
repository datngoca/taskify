package com.example.taskify_backend.request;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddTaskRequest {
    private Integer id;
    private String taskName;
    private String taskDescription;

    public AddTaskRequest(String taskName, String taskDescription) {
        this.taskName = taskName;
        this.taskDescription = taskDescription;
    }
}
