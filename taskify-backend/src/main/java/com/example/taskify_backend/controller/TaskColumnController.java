package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.request.TaskColumnRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.dto.response.TaskColumnResponse;
import com.example.taskify_backend.service.TaskColumnService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/columns")
@RequiredArgsConstructor
public class TaskColumnController {

    private final TaskColumnService taskColumnService;

    @GetMapping("/{boardId}")
    public ApiResponse<List<TaskColumnResponse>> getColumnsByBoard(@PathVariable Long boardId) {
        List<TaskColumnResponse> columns = taskColumnService.getColumnsByBoardId(boardId);
        return ApiResponse.<List<TaskColumnResponse>>builder()
                .result(columns)
                .build();
    }

    @PostMapping
    public ApiResponse<TaskColumnResponse> createColumn(@RequestBody TaskColumnRequest request) {
        TaskColumnResponse createdColumn = taskColumnService.createColumn(request);
        return ApiResponse.<TaskColumnResponse>builder()
                .result(createdColumn)
                .build();
    }

    @PutMapping("/{id}/task_order")
    public ApiResponse<TaskColumnResponse> moveTask(@PathVariable Long id, @RequestBody List<String> cardOrderIds) {
        TaskColumnResponse taskColumn = taskColumnService.moveTask(id, cardOrderIds);
        return ApiResponse.<TaskColumnResponse>builder()
                .result(taskColumn)
                .build();
    }

    @PutMapping("/{columnId}")
    public ApiResponse<TaskColumnResponse> updateColumn(
            @PathVariable Long columnId,
            @RequestBody TaskColumnRequest request) {
        TaskColumnResponse updatedColumn = taskColumnService.updateColumn(columnId, request);
        return ApiResponse.<TaskColumnResponse>builder()
                .result(updatedColumn)
                .build();
    }

    @DeleteMapping("/{columnId}")
    public ApiResponse<Void> deleteColumn(@PathVariable Long columnId) {
        taskColumnService.deleteColumn(columnId);
        return ApiResponse.<Void>builder()
                .message("Delete succesfull")
                .build();
    }
}
