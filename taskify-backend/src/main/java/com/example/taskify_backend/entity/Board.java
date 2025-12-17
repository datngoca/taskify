package com.example.taskify_backend.entity;

import com.example.taskify_backend.common.StringListConverter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Entity
@Table(name = "boards")
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseEntity {
    @Column(nullable = false, length = 100)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore // User sở hữu board này
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 50, nullable = false)
    private BoardType type;

    @Convert(converter = StringListConverter.class)
    @Column(name = "column_order_ids", columnDefinition = "TEXT")
    @Builder.Default
    private List<String> columnOrderIds = new ArrayList<>();

    // Quan hệ 2 chiều (Để xóa Board thì xóa luôn Column con)
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<TaskColumn> columns;

    public Board(String title, User user) {
        this.title = title;
        this.user = user;
    }

    public enum BoardType {
        PUBLIC,
        PRIVATE
    }

    @PrePersist
    public void prePersist() {
        if (this.type == null) {
            this.type = BoardType.PRIVATE;
        }
    }

}
