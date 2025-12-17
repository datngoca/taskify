package com.example.taskify_backend.service;

import com.example.taskify_backend.dto.request.BoardRequest;
import com.example.taskify_backend.dto.response.BoardResponse;
import com.example.taskify_backend.entity.Board;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.AppException;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.mapper.BoardMapper;
import com.example.taskify_backend.repository.BoardRepository;
import com.example.taskify_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {
    final UserContextService userContextService;
    final BoardRepository boardRepository;
    final UserRepository userRepository;
    final BoardMapper boardMapper;

    // --- 1. READ ALL --
    public List<BoardResponse> getAllBoardsByUser() {
        User user = userContextService.getCurrentUser();
        List<Board> boards = boardRepository.findAllByUser(user);
        return boardMapper.toResponse(boards);
    }

    // --- 2. READ ONE --
    public BoardResponse getBoardById(Long id) {
        User user = userContextService.getCurrentUser();
        Board board = boardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
        return boardMapper.toResponse(board);
    }

    @Transactional
    public BoardResponse moveColumn(Long id, List<String> columnOrderIds) {
        User user = userContextService.getCurrentUser();
        Board board = boardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
        board.setColumnOrderIds(columnOrderIds);
        return boardMapper.toResponse(boardRepository.save(board));
    }

    @Transactional
    public BoardResponse createBoard(BoardRequest boardRequest) {
        User user = userContextService.getCurrentUser();
        Board board = boardMapper.toEntity(boardRequest);
        board.setUser(user);
        return boardMapper.toResponse(boardRepository.save(board));
    }

    @Transactional
    public BoardResponse updateBoard(Long id, BoardRequest boardRequest) {
        User user = userContextService.getCurrentUser();
        Board board = boardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

        boardMapper.updateEntityFromRequest(boardRequest, board);

        return boardMapper.toResponse(boardRepository.save(board));
    }

    @Transactional
    public void deleteBoard(Long id) {
        User user = userContextService.getCurrentUser();
        Board board = boardRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

        boardRepository.delete(board);
    }
}
