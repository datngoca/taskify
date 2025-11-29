package com.example.taskify_backend.dto.response;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class ApiResponse <T> {
    private int code;
    private String message;
    private T result;
}
