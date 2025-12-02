package com.example.taskify_backend.controller;

import com.example.taskify_backend.dto.request.SigninRequest;
import com.example.taskify_backend.dto.request.SignupRequest;
import com.example.taskify_backend.dto.response.ApiResponse;
import com.example.taskify_backend.dto.response.JwtResponse;
import com.example.taskify_backend.dto.response.SigninResponse;
import com.example.taskify_backend.dto.response.UserResponse;
import com.example.taskify_backend.service.AuthenticationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

        @Autowired
        private final AuthenticationService authenticationService;

        public AuthController(AuthenticationService authenticationService) {
                this.authenticationService = authenticationService;
        }

        // 1. ĐĂNG KÝ

        @PostMapping("/signup")
        public ApiResponse<String> registerUser(@RequestBody SignupRequest signUpRequest) {
                String jwtResponse = authenticationService.register(signUpRequest);

                return ApiResponse.<String>builder()
                                .code(201)
                                .message(jwtResponse)
                                .build();
        }

        // 2. ĐĂNG NHẬP
        @PostMapping("/signin")
        public ResponseEntity<ApiResponse<SigninResponse>> authenticateUser(@RequestBody SigninRequest loginRequest) {

                JwtResponse loginResult = authenticationService.login(loginRequest);
                ResponseCookie jwtCookie = ResponseCookie.from("refresh_token", loginResult.getRefreshToken())
                                .httpOnly(true) // Chống XSS
                                .secure(false) // Đặt true nếu chạy HTTPS
                                .path("/api/v1/auth/refresh-token") // Cookie chỉ được gửi khi gọi endpoint này
                                .maxAge(24 * 60 * 60) // 1 ngày
                                .sameSite("Strict") // Chống CSRF (Tùy chọn)
                                .build();
                SigninResponse jwtResponse = new SigninResponse(loginResult.getToken());
                return ResponseEntity.ok()
                                // Gắn Cookie vào Header "Set-Cookie"
                                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())

                                // Gắn Body là ApiResponse
                                .body(ApiResponse.<SigninResponse>builder()
                                                .code(200)
                                                .message("User signed in successfully!")
                                                .result(jwtResponse) // Lưu ý: Dùng biến loginResult bạn đã khai báo ở
                                                                     // trên
                                                .build());
        }

        @PostMapping("/logout")
        public ResponseEntity<ApiResponse<String>> logoutUser() {
                // 1. Gọi Service để xóa dữ liệu trong DB (Logic nghiệp vụ)
                authenticationService.logout();

                // 2. Tạo Cookie "chết" để trình duyệt tự xóa (Logic HTTP)
                ResponseCookie cookie = ResponseCookie.from("refresh_token", "") // Giá trị rỗng
                                .httpOnly(true)
                                .secure(false) // Để false nếu chạy localhost (http), true nếu chạy https
                                .path("/api/auth/refresh-token") // ⚠️ QUAN TRỌNG: Phải khớp path lúc tạo
                                .maxAge(0) // ⚠️ QUAN TRỌNG: 0 giây = Xóa ngay lập tức
                                .build();

                // 3. Trả về Response kèm Header xóa cookie
                return ResponseEntity.ok()
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                                .body(ApiResponse.<String>builder()
                                                .code(200)
                                                .message("Logout Succesfull")
                                                .build());
        }

        @GetMapping("/me")
        public ApiResponse<UserResponse> getCurrentUser() {

                return ApiResponse.<UserResponse>builder()
                                .code(200)
                                .message("Get current user successfully!")
                                .result(authenticationService.getCurrentUser())
                                .build();
        }

        @PostMapping("/refresh-token")
        public ResponseEntity<ApiResponse<SigninResponse>> refreshToken(
                        @CookieValue(name = "refresh_token", required = false) String requestRefreshToken) {
                if (requestRefreshToken == null)
                        return ResponseEntity.badRequest().build();
                JwtResponse jwtResponse = authenticationService.refreshToken(requestRefreshToken);

                // 1. Set Refresh Token MỚI vào Cookie
                ResponseCookie cookie = ResponseCookie
                                .from("refresh_token", jwtResponse.getRefreshToken()) // Lấy token mới từ kết quả
                                .httpOnly(true)
                                .secure(false) // nhớ set true nếu chạy HTTPS
                                .path("/api/v1/auth/refresh-token")
                                .maxAge(24 * 60 * 60)
                                .build();
                SigninResponse signinResponse = new SigninResponse(jwtResponse.getToken());
                // 2. Trả Access Token MỚI vào Body
                return ResponseEntity.ok()
                                // Gắn Cookie vào Header "Set-Cookie"
                                .header(HttpHeaders.SET_COOKIE, cookie.toString())

                                // Gắn Body là ApiResponse
                                .body(ApiResponse.<SigninResponse>builder()
                                                .code(200)
                                                .message("User signed in successfully!")
                                                .result(signinResponse) // Lưu ý: Dùng biến loginResult bạn đã khai
                                                                        // báo// ở trên
                                                .build());

        }

}