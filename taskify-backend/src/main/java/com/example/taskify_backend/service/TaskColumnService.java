package com.example.taskify_backend.service;

import com.example.taskify_backend.dto.request.MoveCardDifferentColumnRequest;
import com.example.taskify_backend.dto.request.TaskColumnRequest;
import com.example.taskify_backend.dto.response.TaskColumnResponse;
import com.example.taskify_backend.entity.Board;
import com.example.taskify_backend.entity.Task;
import com.example.taskify_backend.entity.TaskColumn;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.AppException;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.mapper.TaskColumnMapper;
import com.example.taskify_backend.repository.BoardRepository;
import com.example.taskify_backend.repository.TaskColumnRepository;
import com.example.taskify_backend.repository.TaskRepository;

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
    private final TaskRepository taskRepository;
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

    // --- 3. Move cart ---
    @Transactional
    public TaskColumnResponse moveTask(Long id, List<String> cardOrderIds) {
        User user = userContextService.getCurrentUser();
        TaskColumn taskColumn = taskColumnRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.TASK_COLUMN_NOT_FOUND));
        taskColumn.setCardOrderIds(cardOrderIds);
        return taskColumnMapper.toResponse(taskColumnRepository.save(taskColumn));
    }

    @Transactional
    public TaskColumnResponse moveTaskToDifferentColumn(MoveCardDifferentColumnRequest request) {
        User user = userContextService.getCurrentUser();

        // 1. Tối ưu Query: Fetch cả 2 cột trong 1 lần query (dùng IN) thay vì 2 lần
        // findById
        // Giả sử bạn viết thêm method findByIdInAndUser trong Repository
        List<TaskColumn> columns = taskColumnRepository.findByUserAndIdIn(
                user,
                List.of(request.getPrevColumnId(), request.getNextColumnId()));

        if (columns.size() != 2) {
            throw new AppException(ErrorCode.TASK_COLUMN_NOT_FOUND);
        }

        // Xác định cột cũ và cột mới từ list trả về
        TaskColumn prevTaskColumn = columns.stream()
                .filter(c -> c.getId().equals(request.getPrevColumnId()))
                .findFirst().orElseThrow();
        TaskColumn nextTaskColumn = columns.stream()
                .filter(c -> c.getId().equals(request.getNextColumnId()))
                .findFirst().orElseThrow();

        // 2. CRITICAL FIX: Cập nhật Column ID cho Task
        // Phải tìm task đó và set lại column mới cho nó
        Task task = taskRepository.findById(request.getCurrentCardId())
                .orElseThrow(() -> new AppException(ErrorCode.TASK_NOT_FOUND));

        // Cập nhật quan hệ (Foreign Key)
        task.setColumn(nextTaskColumn);

        // 3. Cập nhật thứ tự (Order Ids)
        prevTaskColumn.setCardOrderIds(request.getPrevCardOrderIds());
        nextTaskColumn.setCardOrderIds(request.getNextCardOrderIds());

        // 4. Dirty Checking: Không cần gọi repository.save() thủ công
        // Khi kết thúc hàm @Transactional, Hibernate tự động detect thay đổi
        // trên prevCol, nextCol, và task để sinh ra câu lệnh UPDATE.
        return taskColumnMapper.toResponse(nextTaskColumn);
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
