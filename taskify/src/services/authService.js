import axiosClient from "../api/axiosClient";
import axiosPrivate from "../api/axiosPrivate";

const authService = {
  login: async (username, password) => {
    const response = await axiosClient.post("/auth/signin", {
      username,
      password,
    });
    localStorage.setItem("token", response.data.result.token);
    return response.data.result;
  },

  register: async (username, password) => {
    const response = await axiosClient.post("/auth/signup", {
      username,
      password,
    });
    return response.data.result;
  },

  logout: async () => {
    localStorage.removeItem("token");
    const response = await axiosPrivate.post("/auth/logout");
    return response.data;
  },

  getCurrentUser: async () => {
    const response = await axiosPrivate.get("/auth/me");
    return response.data.result;
  },

  refreshToken: async () => {
    const response = await axiosClient.post("/auth/refresh-token");
    return response.data.result;
  },
};

export default authService;
