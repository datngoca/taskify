import axiosClient from "../api/axiosClient";
const taskService = {
  // Task API
  // 1. Lấy danh sách
  getAll: async () => {
    const response = await axiosClient.get("/task");
    return response.data.result;
  },

  // 2. Thêm mới
  create: async (title) => {
    const response = await axiosClient.post("/task", {
      title,
      status: "todo", // Mặc định backend xử lý, nhưng gửi luôn cho chắc
    });
    return response.data.result;
  },

  // 3. Cập nhật (Sửa tên hoặc Đổi cột)
  update: async (id, taskData) => {
    const response = await axiosClient.put(`task/${id}`, taskData);
    return response.data.result;
  },

  // 4. Xóa
  delete: async (id) => {
    await axiosClient.delete(`task/${id}`);
  },
};

export default taskService;
