package com.example.taskify_backend.service;

import java.time.Instant;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskify_backend.dto.request.SigninRequest;
import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.response.JwtResponse;
import com.example.taskify_backend.dto.response.SigninResponse;
import com.example.taskify_backend.dto.response.UserResponse;
import com.example.taskify_backend.entity.RefreshToken;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.AuthenException;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.exception.NotFoundTaskException;
import com.example.taskify_backend.exception.NotFoundUserException;
import com.example.taskify_backend.repository.RefreshTokenRepository;
import com.example.taskify_backend.repository.UserRepository;
import com.example.taskify_backend.security.jwt.JwtUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationService {
    @Value("${taskify.app.jwtRefreshExpirationMs}") // Ví dụ: 30 ngày (Đọc từ application.properties)
    private Long refreshTokenDurationMs;

    @Autowired
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final AuthenticationManager authenticationManager;

    @Autowired
    private final JwtUtils jwtUtils;

    @Autowired
    private final PasswordEncoder encoder;

    @Autowired
    private final ModelMapper modelMapper;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            PasswordEncoder encoder,
            ModelMapper modelMapper,
            RefreshTokenRepository refreshTokenRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
    }

    public RefreshToken createRefreshToken(int userId) {
        RefreshToken refreshToken = new RefreshToken();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundUserException(ErrorCode.USER_NOT_FOUND));
        // Gán user
        refreshToken.setUser(user);
        // Gán thời gian hết hạn
        refreshToken.setExpiryDate(
                java.time.Instant.now().plusMillis(refreshTokenDurationMs));
        // Gán chuỗi token ngẫu nhiên
        refreshToken.setToken(java.util.UUID.randomUUID().toString());
        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    public String createAuthenticationToken(String username) {
        // Sinh ra token
        return jwtUtils.generateTokenFromUsername(username);
    }

    public String register(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new NotFoundTaskException(ErrorCode.USER_EXISTED);
        }
        // Tạo user mới (Mã hoá password)
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()),
                "USER");

        userRepository.save(user);
        return "User registered successfully!";
    }

    // Login method
    public JwtResponse login(SigninRequest signinRequest) {
        String jwt = createAuthenticationToken(signinRequest.getUsername());
        int userId = userRepository.findByUsername(signinRequest.getUsername())
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.USER_NOT_FOUND))
                .getId();
        // Xoá RT cũ nếu có
        refreshTokenRepository.deleteByUserId(userId);
        RefreshToken refreshToken = createRefreshToken(userId);
        return new JwtResponse(jwt, refreshToken.getToken());
    }

    // Get current user
    public UserResponse getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundTaskException(ErrorCode.USER_NOT_FOUND));
        UserResponse userResponse = toUserResponse(user);
        return userResponse;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            // Token đã hết hạn, xóa khỏi DB và ném lỗi
            refreshTokenRepository.delete(token);
            throw new AuthenException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        return token;
    }

    public JwtResponse refreshToken(String requestRefreshToken) {
        JwtResponse jwtResponse = refreshTokenRepository.findByToken(requestRefreshToken)
                // 2. Kiểm tra hết hạn RT
                .map(this::verifyExpiration)
                .map(token -> {
                    User user = token.getUser();

                    // --- BẮT ĐẦU REFRESH TOKEN ROTATION ---

                    // 3. Xóa Refresh Token cũ khỏi DB (Thu hồi)
                    refreshTokenRepository.deleteByUserId(user.getId());

                    // 4. Tạo mới Access Token
                    String newToken = createAuthenticationToken(user.getUsername());

                    // 5. Tạo mới Refresh Token (Chuỗi UUID ngẫu nhiên)
                    RefreshToken refreshToken = createRefreshToken(user.getId());

                    // --- KẾT THÚC REFRESH TOKEN ROTATION ---

                    // 7. Trả về cặp token mới
                    return new JwtResponse(newToken, refreshToken.getToken());
                })
                .orElseThrow(() -> new AuthenException(ErrorCode.INVALID_REFRESH_TOKEN));
        // ✅ Log kết quả cuối cùng (nhớ @ToString hoặc @Data trong DTO)
        return jwtResponse;
    }
}
