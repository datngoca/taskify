package com.example.taskify_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks") // Tên bảng trùng khớp
public class    Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL bắt buộc dùng cái này mới chuẩn
    private Long id;

    @Column(name = "task_name")
    private String name;


    @Column(name = "task_description")
    private String description;


    // Lưu ý: Java dùng camelCase (isCompleted), DB dùng snake_case (is_completed)
    @Column(name = "is_completed")
    private Boolean isCompleted;


    @CreationTimestamp // Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}