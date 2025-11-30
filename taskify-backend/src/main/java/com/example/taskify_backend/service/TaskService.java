package com.example.taskify_backend.service;

import com.example.taskify_backend.dto.request.AddTaskRequest;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.exception.NotFoundTaskException;
import com.example.taskify_backend.repository.TaskRepository;
import com.example.taskify_backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

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
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.PERMISSION_DENIED));
    }

    public Task getTaskById(int id) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.TASK_NOT_FOUND));
        // KIỂM TRA QUYỀN SỞ HỮU (Chống IDOR)
        if (!task.getUser().getId().equals(currentUser.getId())) {
            throw new NotFoundTaskException(ErrorCode.PERMISSION_DENIED);
        }

        return task;
    }

    public List<Task> getAllUserTasks() {
        User currentUser = getCurrentUser();
        return taskRepository.findByUserId(currentUser.getId());
    }

    public void deleteTaskById(int id) {
        User currentUser = getCurrentUser();
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.TASK_NOT_FOUND));
        if (task.getUser().getId().equals(currentUser.getId())) {
            throw new NotFoundTaskException(ErrorCode.PERMISSION_DENIED);
        }
        taskRepository.deleteById(id);
    }

    public Task updateTaskById(int id, AddTaskRequest task) {
        User currentUser = getCurrentUser();
        Task taskToUpdate = taskRepository.findById(id)
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.TASK_NOT_FOUND));
        // KIỂM TRA QUYỀN SỞ HỮU (Chống IDOR)
        if (!taskToUpdate.getUser().getId().equals(currentUser.getId())) {
            throw new NotFoundTaskException(ErrorCode.PERMISSION_DENIED);
        }
        if (task.getTitle() != null)
            taskToUpdate.setTitle(task.getTitle());
        if (task.getDescription() != null)
            taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setUser(currentUser);
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
        taskToAdd.setUser(currentUser);
        return taskRepository.save(taskToAdd);
    }
}
