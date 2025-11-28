import axios from "axios";

const API_URL = "http://localhost:8080/api/v1/task";

const taskService = {
  // 1. Lấy danh sách
  getAll: async () => {
    const response = await axios.get(API_URL);
    return response.data;
  },

  // 2. Thêm mới
  create: async (title) => {
    const response = await axios.post(API_URL, {
      title,
      status: "todo", // Mặc định backend xử lý, nhưng gửi luôn cho chắc
    });
    return response.data;
  },

  // 3. Cập nhật (Sửa tên hoặc Đổi cột)
  update: async (id, taskData) => {
    const response = await axios.put(`${API_URL}/${id}`, taskData);
    return response.data;
  },

  // 4. Xóa
  delete: async (id) => {
    await axios.delete(`${API_URL}/${id}`);
    return id; // Trả về ID để frontend tự xóa khỏi state
  },
};
export { taskService };
