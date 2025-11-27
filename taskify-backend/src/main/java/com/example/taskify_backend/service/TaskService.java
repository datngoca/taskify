package com.example.taskify_backend.service;

import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.exception.NotFoundTaskException;
import com.example.taskify_backend.repository.TaskRepo;
import com.example.taskify_backend.dto.AddTaskRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepo taskRepo;
    public TaskService(TaskRepo taskRepo) {
        this.taskRepo = taskRepo;
    }

    public Task getTaskById(Integer id) {
        return taskRepo.findById(id).orElseThrow(() -> new NotFoundTaskException("Task with id " + id + " not found")) ;
    }

    public List<Task> getAllTasks() {
        return taskRepo.findAll();
    }

    public void deleteTaskById(Integer id) {
        if (taskRepo.findById(id).isEmpty()) {
           throw new NotFoundTaskException("Task with id " + id + " not found");
        }
        taskRepo.deleteById(id);
    }
    public Task addTask(AddTaskRequest task) {
     Task newTask = new Task();
     newTask.setName(task.getTaskName());
     newTask.setDescription(task.getTaskDescription());
     newTask.setIsCompleted(false);
     return taskRepo.save(newTask);
    }
    public Task updateTaskById(Integer id, AddTaskRequest task) {
        Task currentTask = taskRepo.findById(id).orElseThrow(() -> new NotFoundTaskException("Task with id " + id + " not found"));
        currentTask.setName(task.getTaskName());
        currentTask.setDescription(task.getTaskDescription());
        return taskRepo.save(currentTask);
    }

}
