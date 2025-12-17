package com.example.taskify_backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.taskify_backend.entity.Board;
import com.example.taskify_backend.entity.User;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // 1. Cho chức năng Update/Delete/GetDetail
    // Tìm Task theo ID NHƯNG phải thuộc về User cụ thể
    // Nếu Task tồn tại mà của User khác -> Trả về Empty
    @EntityGraph(attributePaths = "user")
    Optional<Board> findByIdAndUser(Long id, User user);

    // 2. Cho chức năng Get All
    // Lấy tất cả task của user đó
    @EntityGraph(attributePaths = "user")
    List<Board> findAllByUser(User user);
}
