package com.example.taskify_backend.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.request.SigninRequest;

import com.example.taskify_backend.dto.response.UserResponse;
import com.example.taskify_backend.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER") // Default role
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(SignupRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", constant = "USER") // Default role
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromRequest(SigninRequest request, @MappingTarget User entity);

}
