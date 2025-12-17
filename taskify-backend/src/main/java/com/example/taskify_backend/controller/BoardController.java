package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.request.BoardRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.dto.response.BoardResponse;
import com.example.taskify_backend.service.BoardService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/boards")
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ApiResponse<List<BoardResponse>> getAllBoards() {
        List<BoardResponse> boardResponses = boardService.getAllBoardsByUser();
        return ApiResponse.<List<BoardResponse>>builder()
                .result(boardResponses)
                .build();
    }

    @PostMapping
    public ApiResponse<BoardResponse> createBoard(@RequestBody BoardRequest boardRequest) {
        BoardResponse boardResponse = boardService.createBoard(boardRequest);
        return ApiResponse.<BoardResponse>builder()
                .result(boardResponse)
                .build();
    }

    @PutMapping("/{id}/column_order")
    public ApiResponse<BoardResponse> moveColumn(@PathVariable Long id, @RequestBody  List<String> columnOrderIds){
            BoardResponse board = boardService.moveColumn(id, columnOrderIds);
        return ApiResponse.<BoardResponse>builder()
            .result(board)
            .build();
    }

    @GetMapping("/{boardId}")
    public ApiResponse<BoardResponse> getBoardById(@PathVariable Long boardId) {
        BoardResponse boardResponse = boardService.getBoardById(boardId);
        return ApiResponse.<BoardResponse>builder()
                .result(boardResponse)
                .build();
    }

    @PutMapping("/{boardId}")
    public ApiResponse<BoardResponse> updateBoard(@PathVariable Long boardId,
            @Valid @RequestBody BoardRequest boardRequest) {
        BoardResponse boardResponse = boardService.updateBoard(boardId, boardRequest);
        return ApiResponse.<BoardResponse>builder()
                .result(boardResponse)
                .build();
    }

    @DeleteMapping("/{boardId}")
    public ApiResponse<Void> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ApiResponse.<Void>builder()
                .message("Delete successful")
                .build();
    }
}
