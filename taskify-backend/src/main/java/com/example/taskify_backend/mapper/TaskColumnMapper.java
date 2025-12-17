package com.example.taskify_backend.mapper;

import com.example.taskify_backend.dto.request.TaskColumnRequest;
import com.example.taskify_backend.dto.response.TaskColumnResponse;
import com.example.taskify_backend.entity.TaskColumn;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskColumnMapper {
    // Request -> Entity (Bỏ qua ID và User vì sẽ set thủ công)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "board", ignore = true)
    TaskColumn toEntity(TaskColumnRequest request);

    @Mapping(source = "board.id", target = "boardId")
    TaskColumnResponse toResponse(TaskColumn task);

    // Update Entity từ Request (Chỉ update các field cho phép)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    void updateEntityFromRequest(TaskColumnRequest request, @MappingTarget TaskColumn entity);

    // List Entity -> List Response
    List<TaskColumnResponse> toResponse(List<TaskColumn> tasks);
}
