package com.example.taskify_backend.repository;

import com.example.taskify_backend.entity.Task;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.lang.NonNull;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByUserId(Integer userId);

    Task findByIdAndUserId(Integer id, Integer userId);

    void deleteById(@NonNull Integer id);

}
