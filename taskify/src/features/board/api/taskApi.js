import axiosPrivate from "@/api/axiosPrivate";
const taskApi = {
  // Task API
  // 1. Lấy danh sách
  getAllTaskByTaskColumnId: async (idTaskColumn) => {
    const response = await axiosPrivate.get(`/tasks/${idTaskColumn}`);
    return response.data.result;
  },

  // 2. Thêm mới
  create: async (task) => {
    const response = await axiosPrivate.post("/tasks", task);
    return response.data.result;
  },

  // 3. Cập nhật (Sửa tên hoặc Đổi cột)
  update: async (taskData) => {
    const response = await axiosPrivate.put(`tasks/${taskData.id}`, taskData);
    return response.data.result;
  },

  // 4. Xóa
  delete: async (id) => {
    await axiosPrivate.delete(`tasks/${id}`);
  },
};

export { taskApi };
