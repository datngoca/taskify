package com.example.taskify_backend.entity;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task_columns")
@Getter
@Setter
@NoArgsConstructor
public class TaskColumn extends BaseEntity {
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "column_order_ids", columnDefinition = "json")
    private List<String> cardOrderIds = new ArrayList<>();

    // LIÊN KẾT 1 - NHIỀU VỚI CARD
    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Task> tasks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore // User sở hữu board này
    private User user;

    public TaskColumn(String title, Board board) {
        this.title = title;
        this.board = board;
    }
}
