import { createContext, useReducer, useEffect } from "react";

import AuthReducer, { initialState } from "./AuthReducer";
import { authService } from "../services/api";
const AuthContext = createContext(initialState);
const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(AuthReducer, initialState);
  // 1. Kiểm tra phiên đăng nhập khi F5 trang
  useEffect(() => {
    const initializeAuth = async () => {
      try {
        const token = localStorage.getItem("token");
        if (token) {
          const user = await authService.getCurrentUser();
          dispatch({
            type: "INITIALIZE",
            payload: { isAuthenticated: true, [user]: user },
          });
        } else {
          dispatch({
            type: "INITIALIZE",
            payload: { isAuthenticated: false, user: null },
          });
        }
      } catch (err) {
        // Nếu token hết hạn hoặc lỗi
        localStorage.removeItem("token");
        dispatch({
          type: "INITIALIZE",
          payload: { isAuthenticated: false, user: null },
        });
      }
    };

    initializeAuth();
  }, []);
  const login = async (username, password) => {
    try {
      const data = await authService.login(username, password);
      dispatch({
        type: "LOGIN",
        payload: { user: data.user },
      });
    } catch (error) {
      dispatch({
        type: "ERROR",
        payload: { error: error.message },
      });
    }
  };
  const logout = () => {
    authService.logout();
    dispatch({ type: "LOGOUT" });
  };
  const register = async (username, password) => {
    try {
      const data = await authService.register(username, password);
      dispatch({
        type: "REGISTER",
        payload: { user: data, token: data.token },
      });
    } catch (error) {
      dispatch({
        type: "ERROR",
        payload: { error: error.message },
      });
    }
  };
  const value = {
    ...state,
    login,
    logout,
    register,
  };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export { AuthProvider, AuthContext };
