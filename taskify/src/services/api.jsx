import axios from "axios";

const API_URL = "http://localhost:8080/api/v1";

// Tạo một instance riêng của axios để cấu hình global
const axiosClient = axios.create({
  baseURL: API_URL,
});

// --- CẤU HÌNH TỰ ĐỘNG GẮN TOKEN ---
// Mỗi khi gửi request đi, đoạn code này sẽ chạy trước
axiosClient.interceptors.request.use(
  (config) => {
    // Lấy token từ localStorage
    const token = localStorage.getItem("token");

    // Nếu có token, gắn vào Header: Authorization: Bearer <token>
    if (token) {
      config.headers["Authorization"] = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);
const authService = {
  login: async (username, password) => {
    const response = await axiosClient.post("/auth/signin", {
      username,
      password,
    });
    if (response.data.token) {
      localStorage.setItem("user", JSON.stringify(response.data)); // Lưu user info
      localStorage.setItem("token", response.data.token); // Lưu token
    }
    return response.data;
  },

  register: async (username, password) => {
    return await axiosClient.post("/auth/signup", { username, password });
  },

  logout: () => {
    localStorage.removeItem("user");
    localStorage.removeItem("token");
  },
};
const taskService = {
  // 1. Lấy danh sách
  getAll: async () => {
    const response = await axiosClient.get("/task");
    return response.data;
  },

  // 2. Thêm mới
  create: async (title) => {
    const response = await axiosClient.post("/task", {
      title,
      status: "todo", // Mặc định backend xử lý, nhưng gửi luôn cho chắc
    });
    return response.data;
  },

  // 3. Cập nhật (Sửa tên hoặc Đổi cột)
  update: async (id, taskData) => {
    const response = await axiosClient.put(`task/${id}`, taskData);
    return response.data;
  },

  // 4. Xóa
  delete: async (id) => {
    await axiosClient.delete(`task/${id}`);
    return id; // Trả về ID để frontend tự xóa khỏi state
  },
};
export { taskService, authService };
