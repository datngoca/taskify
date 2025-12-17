package com.example.taskify_backend.service;

import com.example.taskify_backend.dto.request.TaskColumnRequest;
import com.example.taskify_backend.dto.response.TaskColumnResponse;
import com.example.taskify_backend.entity.Board;
import com.example.taskify_backend.entity.TaskColumn;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.AppException;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.mapper.TaskColumnMapper;
import com.example.taskify_backend.repository.BoardRepository;
import com.example.taskify_backend.repository.TaskColumnRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskColumnService {

    private final TaskColumnRepository taskColumnRepository;
    private final BoardRepository boardRepository;
    private final UserContextService userContextService;
    private final TaskColumnMapper taskColumnMapper;

    // --- SERVICES ---

    // --- 1. READ ONE ---
    public List<TaskColumnResponse> getColumnsByBoardId(Long boardId) {
        User user = userContextService.getCurrentUser();

        List<TaskColumn> taskColumn = taskColumnRepository.findAllByBoardIdAndUser(boardId, user);
        return taskColumnMapper.toResponse(taskColumn);
    }

    // --- 2. CREATE ---
    @Transactional

    public TaskColumnResponse createColumn(TaskColumnRequest request) {
        User user = userContextService.getCurrentUser();
        Board board = boardRepository.findByIdAndUser(request.boardId(), user)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

        TaskColumn taskColumn = taskColumnMapper.toEntity(request);

        taskColumn.setUser(user);
        taskColumn.setBoard(board);

        TaskColumn taskColumnSaved = taskColumnRepository.save(taskColumn);

        List<String> currentOrder = board.getColumnOrderIds();
        currentOrder.add(taskColumnSaved.getId().toString());
        board.setColumnOrderIds(currentOrder);
        boardRepository.save(board);

        return taskColumnMapper.toResponse(taskColumnSaved);
    }

    @Transactional
    public TaskColumnResponse moveTask(Long id, List<String> cardOrderIds) {
        User user = userContextService.getCurrentUser();
        TaskColumn taskColumn = taskColumnRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_COLUMN_NOT_FOUND));
        taskColumn.setCardOrderIds(cardOrderIds);
        return taskColumnMapper.toResponse(taskColumnRepository.save(taskColumn));
    }

    // --- 3. UPDATE ---
    @Transactional
    public TaskColumnResponse updateColumn(Long id, TaskColumnRequest request) {
        User user = userContextService.getCurrentUser();
        TaskColumn taskColumn = taskColumnRepository.findByIdAndBoardIdAndUser(id, request.boardId(), user)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_COLUMN_NOT_FOUND));
        Board board = boardRepository.findByIdAndUser(request.boardId(), user)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

        taskColumnMapper.updateEntityFromRequest(request, taskColumn);
        taskColumn.setBoard(board);

        return taskColumnMapper.toResponse(taskColumnRepository.save(taskColumn));
    }

    @Transactional
    public void deleteColumn(Long columnId) {
        User user = userContextService.getCurrentUser();
        TaskColumn taskColumn = taskColumnRepository.findByIdAndUser(columnId, user)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_COLUMN_NOT_FOUND));
        taskColumnRepository.delete(taskColumn);
    }
}
