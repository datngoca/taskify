package com.example.taskify_backend.controller;

import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> findAll() {
        return taskService.getAllTasks();
    }
    @GetMapping("/{id}")
    public Task findTaskById(@PathVariable Integer id ) {
        return taskService.getTaskById(id);
    }
}
