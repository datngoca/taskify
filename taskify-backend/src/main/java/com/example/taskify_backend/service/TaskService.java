package com.example.taskify_backend.service;

import com.example.taskify_backend.dto.AddTaskRequest;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.exception.NotFoundTaskException;
import com.example.taskify_backend.repository.TaskRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepo taskRepo;

    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public Task getTaskById(Integer id) {
        return taskRepo.findById(id).orElseThrow(() -> new NotFoundTaskException("Task with id " + id + " not found"));
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public void deleteTaskById(Integer id) {
        if (!taskRepo.existsById(id))
            throw new NotFoundTaskException("Task id " + id + " not found");
        taskRepo.deleteById(id);
    }

    public Task updateTaskById(Integer id, AddTaskRequest task) {
        Task taskToUpdate = taskRepo.findById(id)
                .orElseThrow(() -> new NotFoundTaskException("Task id " + id + " not found"));
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
