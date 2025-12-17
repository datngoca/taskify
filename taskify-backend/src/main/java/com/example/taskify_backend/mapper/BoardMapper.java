package com.example.taskify_backend.mapper;

import com.example.taskify_backend.dto.request.BoardRequest;
import com.example.taskify_backend.dto.response.BoardResponse;
import com.example.taskify_backend.entity.Board;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BoardMapper {
    // Request -> Entity (Bỏ qua User vì sẽ set thủ công)
    @Mapping(target = "user", ignore = true)
    Board toEntity(BoardRequest request);

    BoardResponse toResponse(Board task);

    // Update Entity từ Request (Chỉ update các field cho phép)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(BoardRequest request, @MappingTarget Board entity);

    // List Entity -> List Response
    List<BoardResponse> toResponse(List<Board> tasks);
}
