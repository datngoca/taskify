package com.example.taskify_backend.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.taskify_backend.exception.AppException;
import com.example.taskify_backend.exception.ErrorCode;

import java.security.Key;
import java.util.Date;
import java.nio.charset.StandardCharsets;

@Component
public class JwtUtils {
    @Value("${taskify.app.jwtSecret}")
    private String jwtSecret;
    @Value("${taskify.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public Key getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // 1. TẠO TOKEN TỪ THÔNG TIN USER
    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 2. LẤY USERNAME TỪ TOKEN
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // 3. KIỂM TRA TOKEN CÓ HỢP LỆ KHÔNG
    public void validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
        } catch (ExpiredJwtException e) {
            // Đây là lỗi hết hạn -> Ném lỗi Custom của bạn
            throw new AppException(ErrorCode.TOKEN_EXPIRED);
            // Hoặc tạo ErrorCode riêng: ErrorCode.TOKEN_EXPIRED
        } catch (JwtException | IllegalArgumentException e) {
            // Các lỗi khác (sai chữ ký, rỗng...) -> Ném lỗi chung
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }
    }
}
