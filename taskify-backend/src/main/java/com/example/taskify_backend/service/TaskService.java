package com.example.taskify_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskify_backend.dto.request.TaskRequest;
import com.example.taskify_backend.dto.response.TaskResponse;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.entity.TaskColumn;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.AppException;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.mapper.TaskMapper;
import com.example.taskify_backend.repository.TaskColumnRepository;
import com.example.taskify_backend.repository.TaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskColumnRepository taskColumnRepository;
    private final UserContextService userContextService;
    private final TaskMapper taskMapper;

    // --- SERVICES ---

    // --- 1. READ ONE ---
    public List<TaskResponse> getAllTaskByColumnId(Long columnId){
        User currentUser = userContextService.getCurrentUser();

        List<Task> tasks = taskRepository.findAllByColumnIdAndUser(columnId, currentUser);

        return taskMapper.toResponse(tasks);
    }

    // --- 1. READ ONE ---
    public TaskResponse getTaskById(Long id) {
        User currentUser = userContextService.getCurrentUser();

        Task task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
        return taskMapper.toResponse(task);
    }

    // --- 2. READ ALL (My Tasks) ---
    public List<TaskResponse> getAllUserTasks() {
        User currentUser = userContextService.getCurrentUser();

        List<Task> tasks = taskRepository.findAllByUser(currentUser);

        return taskMapper.toResponse(tasks);
    }

    @Transactional
    // --- 3. CREATE ---
    public TaskResponse createTask(TaskRequest request) {
        User currentUser = userContextService.getCurrentUser();

        TaskColumn taskColumn = taskColumnRepository.findByIdAndUser(request.columnId(), currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_COLUMN_NOT_FOUND));

        Task task = taskMapper.toEntity(request);
        task.setUser(currentUser);
        task.setColumn(taskColumn);

        Task taskSaved = taskRepository.save(task);

        List<String> currentOrder = taskColumn.getCardOrderIds();
        currentOrder.add(taskSaved.getId().toString());
        taskColumn.setCardOrderIds(currentOrder);
        taskColumnRepository.save(taskColumn);

        return taskMapper.toResponse(taskSaved);
    }

    // --- 4. UPDATE ---
    @Transactional
    public TaskResponse updateTaskById(Long id, TaskRequest request) {
        User currentUser = userContextService.getCurrentUser();

        TaskColumn taskColumn = taskColumnRepository.findByIdAndUser(request.columnId(), currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_COLUMN_NOT_FOUND));

        Task task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        taskMapper.updateEntityFromRequest(request, task);

        task.setColumn(taskColumn);

        return taskMapper.toResponse(taskRepository.save(task));
    }

    // --- 5. DELETE ---
    @Transactional
    public void deleteTaskById(Long id) {
        User currentUser = userContextService.getCurrentUser();

        Task task = taskRepository.findByIdAndUser(id, currentUser)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));
        taskRepository.delete(task);
    }
}
