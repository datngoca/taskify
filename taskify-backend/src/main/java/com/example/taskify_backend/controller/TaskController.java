package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.request.AddTaskRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/task")
@CrossOrigin(origins = "http://localhost:5173")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ApiResponse<List<Task>> findAll() {
        return ApiResponse.<List<Task>>builder()
                .code(200)
                .message("Get All Tasks Success")
                .result(taskService.getAllTasks())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Task> findTaskById(@PathVariable Integer id) {
        return taskService.getTaskById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTaskById(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
    }

    @PostMapping
    public Task addTask(@RequestBody AddTaskRequest task) {
        return taskService.addTask(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@RequestBody AddTaskRequest task, @PathVariable Integer id) {
        return taskService.updateTaskById(id, task);
    }

}
