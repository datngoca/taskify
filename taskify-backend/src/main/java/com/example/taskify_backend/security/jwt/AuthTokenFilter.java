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

import com.example.taskify_backend.exception.AppException;
import com.example.taskify_backend.security.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.taskify_backend.dto.response.ApiResponse;

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
        // 1. Lấy đường dẫn request hiện tại
        String requestPath = request.getServletPath();

        // 2. NẾU LÀ REFRESH TOKEN -> CHO QUA LUÔN (SKIP LOGIC CHECK JWT)
        // Lưu ý: Đường dẫn phải khớp chính xác với Controller của bạn
        if (requestPath.equals("/api/v1/auth/refresh-token")) {
            filterChain.doFilter(request, response);
            return; // Kết thúc hàm này ngay, không chạy đoạn parseJwt bên dưới nữa
        }

        try {
            String jwt = parseJwt(request);

            if (jwt != null) {
                // Gọi hàm validate (giờ hàm này sẽ ném lỗi nếu sai)
                jwtUtils.validateJwtToken(jwt);

                // Nếu không bị lỗi thì chạy tiếp logic lấy user
                String username = jwtUtils.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            // Cho phép request đi tiếp nếu không có lỗi
            filterChain.doFilter(request, response);

        } catch (AppException e) {
            // BẮT LỖI Ở ĐÂY: Khi token hết hạn hoặc sai
            sendErrorResponse(response, e);
        } catch (Exception e) {
            // Các lỗi hệ thống khác
            System.out.println("Cannot set user authentication: " + e.getMessage());
            filterChain.doFilter(request, response); // Vẫn cho đi tiếp để Security xử lý (thường là trả về 401)
        }
    }

    // --- HÀM BỔ TRỢ ĐỂ VIẾT JSON ---
    private void sendErrorResponse(HttpServletResponse response, AppException e) throws IOException {
        response.setStatus(e.getErrorCode().getStatusCode().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse<Void> apiResponse = ApiResponse.<Void>builder()
                .code(e.getErrorCode().getCode())
                .message(e.getErrorCode().getMessage())
                .build();

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(apiResponse));
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