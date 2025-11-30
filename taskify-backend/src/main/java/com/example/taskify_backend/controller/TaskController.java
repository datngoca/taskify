package com.example.taskify_backend.controller;
import com.example.taskify_backend.dto.request.AddTaskRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.service.TaskService;
import jakarta.validation.Valid;
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
                .result(taskService.getAllUserTasks())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Task> findTaskById(@PathVariable Integer id) {
        return ApiResponse.<Task>builder()
                .code(200)
                .message("Get Task by id: " + id + " Success")
                .result(taskService.getTaskById(id))
                .build();
    }

    @DeleteMapping("/delete/{id}")
    public ApiResponse<String> deleteTaskById(@PathVariable Integer id) {
        taskService.deleteTaskById(id);
        return ApiResponse.<String>builder()
                .code(200)
                .message("Delete Task by id: " + id + " Success")
                .result("Delete Success")
                .build();
    }


    @PostMapping
    public ApiResponse<Task> addTask(@RequestBody @Valid AddTaskRequest task) {
        return ApiResponse.<Task>builder()
                .code(201)
                .message("Add Task Success")
                .result(taskService.addTask(task))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<Task> updateTask(@RequestBody @Valid AddTaskRequest task, @PathVariable Integer id) {
        return ApiResponse.<Task>builder()
                .code(200)
                .message("Update Task by id: " + id + " Success")
                .result(taskService.updateTaskById(id, task))
                .build();
    }
}
