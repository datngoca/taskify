package com.example.taskify_backend.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.taskify_backend.dto.request.TaskRequest;
import com.example.taskify_backend.dto.response.TaskResponse;
import com.example.taskify_backend.entity.Task;


@Mapper(componentModel = "spring")
public interface TaskMapper {
    // Request -> Entity (Bỏ qua ID và User vì sẽ set thủ công)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Task toEntity(TaskRequest request);

    @Mapping(source = "column.id", target = "columnId")
    TaskResponse toResponse(Task task);

    // Update Entity từ Request (Chỉ update các field cho phép)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(TaskRequest request, @MappingTarget Task entity);

    // List Entity -> List Response
    List<TaskResponse> toResponse(List<Task> tasks);
}
