import { AUTH_ACTIONS } from "./actionType";
export const initialState = {
  isAuthenticated: false,
  isInitialized: false,
  user: null,
  error: null,
};

export const authReducer = (state, action) => {
  switch (action.type) {
    case AUTH_ACTIONS.INITIALIZE:
      return {
        ...state,
        isAuthenticated: action.payload.isAuthenticated,
        isInitialized: true,
        user: action.payload.user,
      };
    case AUTH_ACTIONS.LOGIN:
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload.user,
      };
    case AUTH_ACTIONS.LOGOUT:
      return {
        ...state,
        isAuthenticated: false,
        user: null,
      };
    case AUTH_ACTIONS.REGISTER:
      return {
        ...state,
        isAuthenticated: true,
        user: action.payload.user,
        token: action.payload.token,
      };
    case AUTH_ACTIONS.ERROR:
      return {
        ...state,
        error: action.payload.error,
      };
    default:
      return state;
  }
};