import axiosPrivate from "@/api/axiosPrivate";
const boardApi = {
  fetchBoardData: async () => {
    const response = await axiosPrivate.get("/boards");
    return response.data.result;
  },

  fetchDetails: async (id) => {
    const response = await axiosPrivate.get(`/boards/${id}`);
    return response.data.result;
  },

  createNewBoard: async (boardData) => {
    const response = await axiosPrivate.post("/boards", boardData);
    return response.data.result;
  },

  updateColumnOrder: async (id, newColumnOrderIds) => {
    const response = await axiosPrivate.put(
      `/boards/${id}/column_order`,
      newColumnOrderIds
    );
    return response.data.result;
  },
};
export { boardApi };
