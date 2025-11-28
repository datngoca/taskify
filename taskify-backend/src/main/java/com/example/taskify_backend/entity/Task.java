package com.example.taskify_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks") // Tên bảng trùng khớp
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL bắt buộc dùng cái này mới chuẩn
    private Long id;

    private String title;

    private String description;

    private Boolean isCompleted;

    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}