package com.example.taskify_backend.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.taskify_backend.dto.request.SigninRequest;
import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.response.JwtResponse;
import com.example.taskify_backend.dto.response.UserResponse;
import com.example.taskify_backend.entity.User;
import com.example.taskify_backend.exception.ErrorCode;
import com.example.taskify_backend.exception.NotFoundTaskException;
import com.example.taskify_backend.repository.UserRepository;
import com.example.taskify_backend.security.jwt.JwtUtils;

@Service
public class AuthenticationService {
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

    public AuthenticationService(UserRepository userRepository,
            AuthenticationManager authenticationManager,
            JwtUtils jwtUtils,
            PasswordEncoder encoder,
            ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
    }

    public UserResponse toUserResponse(User user) {
        return modelMapper.map(user, UserResponse.class);
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

    // Register method
    public JwtResponse login(SigninRequest signinRequest) {
        // Xác thực username/password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinRequest.getUsername(), signinRequest.getPassword()));

        // Nếu xác thực thành công, lưu vào context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Sinh ra token
        String jwt = jwtUtils.generateJwtToken(authentication);

        return new JwtResponse(jwt, signinRequest.getUsername());
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

}
