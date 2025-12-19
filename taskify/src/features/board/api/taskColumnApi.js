import axiosPrivate from "@/api/axiosPrivate";

const taskColumnApi = {
  getAllTaskColumnByBoardId: async (boardId) => {
    const response = await axiosPrivate.get(`/columns/${boardId}`);
    return response.data.result;
  },

  createTaskColumn: async (taskColumnData) => {
    const response = await axiosPrivate.post("/columns", taskColumnData);
    return response.data.result;
  },

  updateTaskColumn: async (id, taskColumnData) => {
    const response = await axiosPrivate.put(`/columns/${id}`, taskColumnData);
    return response.data.result;
  },

  deleteTaskColumn: async (id) => {
    await axiosPrivate.delete(`/columns/${id}`);
  },

  moveCardToDifferentColumn: async (data) => {
    const response = await axiosPrivate.put("/columns/moving_task", data);
    return response.data.result;
  },

  updateColumnCardOrder: async (columnId, cardOrderIds) => {
    const response = await axiosPrivate.put(`/columns/${columnId}/task_order`, {
      cardOrderIds,
    });
    return response.data.result;
  },
};
export { taskColumnApi };
