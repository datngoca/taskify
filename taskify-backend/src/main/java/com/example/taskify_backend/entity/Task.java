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
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL bắt buộc dùng cái này mới chuẩn
    private Long id;

    // 👇 KHỚP 1: Trong ảnh cột là 'name'
    @Column(name = "name")
    private String name;

    // 👇 KHỚP 2: Trong ảnh cột là 'description'
    @Column(name = "description")
    private String description;

    // 👇 KHỚP 3: Trong ảnh cột là 'is_completed'
    // Lưu ý: Java dùng camelCase (isCompleted), DB dùng snake_case (is_completed)
    @Column(name = "is_completed")
    private Boolean isCompleted;

    // 👇 KHỚP 4: Trong ảnh cột là 'created_at'
    @CreationTimestamp // Tự động điền ngày giờ khi tạo mới
    @Column(name = "created_at", updatable = false)
    private Date createdAt;
}