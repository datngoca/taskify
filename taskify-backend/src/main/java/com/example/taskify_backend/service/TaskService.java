package com.example.taskify_backend.service;

import com.example.taskify_backend.dto.request.AddTaskRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.exception.NotFoundTaskException;
import com.example.taskify_backend.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepo;

    public TaskService(TaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    // ================== Service ================================

    public ApiResponse<Task> getTaskById(Integer id) {
        ApiResponse<Task> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setMessage("Get Task by id: "+ id + " Success");
        apiResponse.setResult(taskRepo.findById(id)
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.TASK_NOT_FOUND)));

        return apiResponse;
    }

    public ApiResponse<List<Task>> getAllTasks() {
        ApiResponse<List<Task>> response = new ApiResponse<>();
        response.setCode(200);
        response.setMessage("Get all tasks success");
        response.setResult(taskRepo.findAll());
        return response;
    }

    public void deleteTaskById(Integer id) {
        if (!taskRepo.existsById(id))
            throw new NotFoundTaskException(ErrorCode.TASK_NOT_FOUND);
        taskRepo.deleteById(id);
    }

    public Task updateTaskById(Integer id, AddTaskRequest task) {
        Task taskToUpdate = taskRepo.findById(id)
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.TASK_NOT_FOUND));
        if (task.getTitle() != null)
            taskToUpdate.setTitle(task.getTitle());
        if (task.getDescription() != null)
            taskToUpdate.setDescription(task.getDescription());
        return taskRepo.save(taskToUpdate);
    }

    public Task addTask(AddTaskRequest task) {

        Task taskToAdd = new Task();
        if (task.getTitle() != null)
            taskToAdd.setTitle(task.getTitle());
        if (task.getDescription() != null)
            taskToAdd.setDescription(task.getDescription());
        taskToAdd.setStatus("todo");
        return taskRepo.save(taskToAdd);
    }
}
