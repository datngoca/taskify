package com.example.taskify_backend.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.lang.NonNull;

import com.example.taskify_backend.security.UserDetailsServiceImpl;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    // HÀM CHÍNH: CHẠY MỖI KHI CÓ REQUEST
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // 1. Lấy token từ Header (bỏ chữ 'Bearer ')
            String jwt = parseJwt(request);

            // 2. Kiểm tra token có hợp lệ không
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {

                // 3. Lấy username từ token
                String username = jwtUtils.getUserNameFromJwtToken(jwt);

                // 4. Lấy thông tin User đầy đủ từ DB
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // 5. Tạo đối tượng xác thực (Authentication)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 6. Lưu vào Security Context (Báo cho Spring biết: "Ông này đã đăng nhập
                // rồi!")
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            System.out.println("Cannot set user authentication: " + e.getMessage());
        }

        // Cho phép request đi tiếp vào Controller
        filterChain.doFilter(request, response);
    }

    // Helper: Lấy chuỗi token sạch từ header "Authorization: Bearer abcd..."
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Cắt bỏ 7 ký tự đầu ("Bearer ")
        }

        return null;
    }
}