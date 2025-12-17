package com.example.taskify_backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.taskify_backend.entity.RefreshToken;
import com.example.taskify_backend.entity.User;

import jakarta.transaction.Transactional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserId(Long userId);

    @Transactional
    void deleteByUserId(Long userId);

    void deleteByUser(User user);

}
