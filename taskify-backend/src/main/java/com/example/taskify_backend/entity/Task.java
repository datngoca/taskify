package com.example.taskify_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    @ManyToOne(fetch = FetchType.LAZY) // Khi lấy Task, khoan hãy lấy User vội (để nhẹ máy)
    @JoinColumn(name = "user_id") // Tên cột khóa ngoại trong DB sẽ là user_id
    @JsonIgnore // Quan trọng: Khi trả về JSON task, đừng kèm theo cả cục User (tránh lặp vô
                // tận)
    private User user;

    @CreationTimestamp
    @Column(updatable = false)
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;
}