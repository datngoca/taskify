package com.example.taskify_backend.repository;

import com.example.taskify_backend.entity.TaskColumn;
import com.example.taskify_backend.entity.User;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskColumnRepository extends JpaRepository<TaskColumn, Long> {
    // 1. Cho chức năng Update/Delete/GetDetail
    // Tìm Task theo ID NHƯNG phải thuộc về User cụ thể
    // Nếu Task tồn tại mà của User khác -> Trả về Empty
    @EntityGraph(attributePaths = "user")
    Optional<TaskColumn> findByIdAndBoardIdAndUser(Long id, Long boardId, User user);

    @EntityGraph(attributePaths = "user")
    Optional<TaskColumn> findByIdAndUser(Long id, User user);

    // 2. Cho chức năng Get All
    // Lấy tất cả task của user đó
    @EntityGraph(attributePaths = "user")
    List<TaskColumn> findAllByBoardIdAndUser(Long boardId, User user);

    List<TaskColumn> findByUserAndIdIn(User user, Collection<Long> ids);

}
