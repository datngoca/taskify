package com.example.taskify_backend.service;

import java.time.Instant;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.taskify_backend.dto.request.SigninRequest;
import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.response.JwtResponse;
import com.example.taskify_backend.dto.response.UserResponse;
import com.example.taskify_backend.entity.RefreshToken;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.AppException;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.mapper.UserMapper;
import com.example.taskify_backend.repository.RefreshTokenRepository;
import com.example.taskify_backend.repository.UserRepository;
import com.example.taskify_backend.security.UserDetailsImpl;
import com.example.taskify_backend.security.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {

    @Value("${taskify.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder encoder;
    private final UserContextService userContextService;
    private final UserMapper userMapper;

    public String register(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.username())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }
        User user = userMapper.toEntity(signUpRequest);
        user.setPassword(encoder.encode(signUpRequest.password()));
        
        userRepository.save(user);
        return "User registered successfully!";
    }

    @Transactional
    public JwtResponse login(SigninRequest signinRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        signinRequest.username(),
                        signinRequest.password()
                ));

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String jwt = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userDetails.getId())
                .map(existingToken -> {
                    existingToken.setToken(UUID.randomUUID().toString());
                    existingToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
                    return refreshTokenRepository.save(existingToken);
                })
                .orElseGet(() -> createRefreshToken(userDetails.getId()));

        return new JwtResponse(jwt, refreshToken.getToken());
    }

    public UserResponse getCurrentUser() {
        User user = userContextService.getCurrentUser();
        return userMapper.toResponse(user);
    }

    public JwtResponse refreshToken(String requestRefreshToken) {
        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(token -> {
                    User user = token.getUser();
                    refreshTokenRepository.deleteByUserId(user.getId());

                    String newToken = jwtUtils.generateTokenFromUsername(user.getUsername());
                    RefreshToken refreshToken = createRefreshToken(user.getId());

                    return new JwtResponse(newToken, refreshToken.getToken());
                })
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));
    }

    public void logout() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!"anonymousUser".equals(principal.toString())) {
            Long userId = ((UserDetailsImpl) principal).getId();
            refreshTokenRepository.deleteByUserId(userId);
        }
    }

    private RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        
        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return token;
    }
}