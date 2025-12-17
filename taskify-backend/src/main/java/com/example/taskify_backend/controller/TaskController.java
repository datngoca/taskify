package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.request.TaskRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.dto.response.TaskResponse;
import com.example.taskify_backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/column/{columnId}")
    public ApiResponse<List<TaskResponse>> findAllByTaskColumnId(@PathVariable Long columnId) {
        List<TaskResponse> tasks = taskService.getAllTaskByColumnId(columnId);
        return ApiResponse.<List<TaskResponse>>builder()
                .code(200)
                .message("Get All Tasks Success")
                .result(tasks)
                .build();
    }

    @GetMapping
    public ApiResponse<List<TaskResponse>> findAll() {
        return ApiResponse.<List<TaskResponse>>builder()
                .code(200)
                .message("Get All Tasks Success")
                .result(taskService.getAllUserTasks())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskResponse> findTaskById(@PathVariable Long id) {
        return ApiResponse.<TaskResponse>builder()
                .code(200)
                .message("Get Task by id: " + id + " Success")
                .result(taskService.getTaskById(id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteTaskById(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Delete Task by id: " + id + " Success")
                .result("Delete Success")
                .build();
    }

    @PostMapping
    public ApiResponse<TaskResponse> addTask(@RequestBody @Valid TaskRequest task) {
        return ApiResponse.<TaskResponse>builder()
                .code(201)
                .message("Add Task Success")
                .result(taskService.createTask(task))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<TaskResponse> updateTask(@RequestBody @Valid TaskRequest task, @PathVariable Long id) {
        return ApiResponse.<TaskResponse>builder()
                .code(200)
                .message("Update Task by id: " + id + " Success")
                .result(taskService.updateTaskById(id, task))
                .build();
    }
}