import { createContext, useReducer } from "react";

import { authReducer, initialState } from "./authReducer";
import {authApi} from "@/features/auth/api";
const AuthContext = createContext(initialState);
const AuthProvider = ({ children }) => {
  const [state, dispatch] = useReducer(authReducer, initialState);

  const initialize = (isAuthenticated, user) => {
    dispatch({
      type: "INITIALIZE",
      payload: { isAuthenticated, user },
    });
  };

  const login = async (username, password) => {
    try {
      const data = await authApi.login(username, password);
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
    authApi.logout();
    dispatch({ type: "LOGOUT" });
  };
  const register = async (username, password) => {
    try {
      const data = await authApi.register(username, password);
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
    initialize,
  };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export { AuthProvider, AuthContext };
