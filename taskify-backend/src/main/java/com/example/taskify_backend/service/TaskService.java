package com.example.taskify_backend.service;

import com.example.taskify_backend.dto.request.AddTaskRequest;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.NotFoundTaskException;
import com.example.taskify_backend.repository.TaskRepository;
import com.example.taskify_backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TaskService {
    @Autowired
    private final TaskRepository taskRepository;
    @Autowired
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    // --- HÀM PHỤ TRỢ: Lấy User đang đăng nhập ---
    private User getCurrentUser() {
        // Lấy username từ "Token" trong SecurityContext
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();

        // Tìm User trong DB
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public Task getTaskById(Integer id) {
        User currentUser = getCurrentUser();
        return taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundTaskException("Task with id " + id + " not found"));
    }

    public List<Task> getAllTasks() {
        User currentUser = getCurrentUser();
        return taskRepository.findByUserId(currentUser.getId());
    }

    public void deleteTaskById(Integer id) {
        User currentUser = getCurrentUser();

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found"));

        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to delete this task");
        }
        taskRepository.delete(task);
    }

    public Task updateTaskById(Integer id, AddTaskRequest task) {
        User currentUser = getCurrentUser();
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundTaskException("Task id " + id + " not found"));
        // KIỂM TRA QUYỀN SỞ HỮU (Chống IDOR)
        if (!taskToUpdate.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have permission to edit this task");
        }
        if (task.getTitle() != null)
            taskToUpdate.setTitle(task.getTitle());
        if (task.getDescription() != null)
            taskToUpdate.setDescription(task.getDescription());
        return taskRepository.save(taskToUpdate);
    }

    public Task addTask(AddTaskRequest task) {
        Task taskToAdd = new Task();
        User currentUser = getCurrentUser();
        taskToAdd.setUser(currentUser);
        if (task.getTitle() != null)
            taskToAdd.setTitle(task.getTitle());
        if (task.getDescription() != null)
            taskToAdd.setDescription(task.getDescription());
        taskToAdd.setStatus("todo");
        return taskRepository.save(taskToAdd);
    }
}
