import axios from "axios";

const API_URL = "http://localhost:8080/api/v1";

const axiosClient = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true, // Để gửi Cookie Refresh Token
});

// 1. Request Interceptor: Tự động gắn Token
axiosClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 2. Response Interceptor: Tự động Refresh Token khi gặp lỗi 401
axiosClient.interceptors.response.use(
  (response) => response, // Thành công thì trả về luôn
  async (error) => {
    const originalRequest = error.config;

    // Nếu lỗi 401 và chưa từng thử lại
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      try {
        // Gọi API Refresh Token (Cookie tự gửi đi)
        // Lưu ý: Dùng instance axios mới hoặc chính nó nhưng cẩn thận loop
        // Ở đây ta dùng axiosClient luôn vì endpoint refresh không cần Access Token
        const response = await axiosClient.post(
          "/auth/refresh-token",
          {},
          {
            headers: {
              Authorization: "", // Ghi đè header global: Không gửi token hết hạn lên
            },
          }
        );
        // Lấy token mới (Backend trả về cấu trúc nào thì sửa chỗ này)
        const newToken = response.data.result.token; // Ví dụ theo code cũ của bạn

        // Lưu lại token mới
        localStorage.setItem("token", newToken);

        // Gắn header mới vào request cũ
        originalRequest.headers["Authorization"] = `Bearer ${newToken}`;
        // Gọi lại request cũ
        return axiosClient(originalRequest);
      } catch (refreshError) {
        // Nếu Refresh Token cũng lỗi (Hết hạn hẳn) -> Logout bắt buộc
        console.error("Phiên đăng nhập hết hạn.");
        localStorage.removeItem("token");

        // Chuyển hướng về trang login (Cách đơn giản nhất để tách khỏi React Context)
        window.location.href = "/login";

        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosClient;
