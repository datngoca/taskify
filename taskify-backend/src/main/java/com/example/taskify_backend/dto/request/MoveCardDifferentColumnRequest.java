package com.example.taskify_backend.dto.request;

import java.util.List;

import lombok.*;

@Getter
public class MoveCardDifferentColumnRequest {
    private Long currentCardId;
    private Long prevColumnId;
    private List<String> prevCardOrderIds;
    private Long nextColumnId;
    private List<String> nextCardOrderIds;
}
