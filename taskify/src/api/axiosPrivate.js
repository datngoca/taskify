import axios from "axios";
import axiosClient from "./axiosClient";

const API_URL = "http://localhost:8080/api/v1";

const axiosPrivate = axios.create({
  baseURL: API_URL,
  headers: {
    "Content-Type": "application/json",
  },
  withCredentials: true,
});

// 1. Request Interceptor: Tự động gắn Token
axiosPrivate.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);
let isRefreshing = false;
let refreshPromise = null;

// 2. Response Interceptor: Tự động Refresh Token khi gặp lỗi 401
axiosPrivate.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    // Nếu lỗi 401 và chưa từng thử lại
    if (error.response?.status === 401 && !originalRequest._retry) {
      if (isRefreshing) {
            // Nếu đang refresh, thì đợi cái promise kia xong rồi dùng token mới
            return refreshPromise.then(token => {
                originalRequest.headers.Authorization = `Bearer ${token}`;
                return axios(originalRequest);
            });
        }
      originalRequest._retry = true;
      isRefreshing = true;

      try {
        // Gọi API Refresh Token bằng axiosClient (public)
        const response = await axiosClient.post("/auth/refresh-token");
        
        const newToken = response.data.result.token;

        // Lưu lại token mới
        localStorage.setItem("token", newToken);

        // Gắn header mới vào request cũ
        originalRequest.headers["Authorization"] = `Bearer ${newToken}`;
        
        // Gọi lại request cũ bằng axiosPrivate
        return axiosPrivate(originalRequest);
      } catch (refreshError) {
        // Nếu Refresh Token cũng lỗi (Hết hạn hẳn) -> Logout bắt buộc
        console.error("Phiên đăng nhập hết hạn.");
        localStorage.removeItem("token");
        alert(refreshError);
        // Chuyển hướng về trang login
        window.location.href = "/login";

        return Promise.reject(refreshError);
      }
    }

    return Promise.reject(error);
  }
);

export default axiosPrivate;
