package com.example.taskify_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.taskify_backend.security.jwt.AuthTokenFilter;

@Configuration
public class SecurityConfig {
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // 1. Khai báo công cụ mã hoá mật khẩu (BCrypt)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Khai báo AuthenticationManager (để xử lý đăng nhập)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 3. Cấu hình bộ lọc (Filter Chain) - Quy định ai được vào đâu
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Tắt chống giả mạo request (do ta dùng JWT)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Không
                                                                                                              // // JWT)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**").permitAll() // Cho phép vào trang Đăng nhập/Đăng ký tự do
                        .requestMatchers("/api/v1/task/**").authenticated() // Các trang Task bắt buộc phải đăng nhập
                        .anyRequest().permitAll() // Các cái khác (nếu có) cứ mở tạm
                );
        // Chèn Filter của ta vào trước Filter mặc định của Spring
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}