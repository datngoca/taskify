import { Navigate } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.jsx";
const RedirectIfLoggedIn = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return !isAuthenticated ? <>{children}</> : <Navigate to="/" replace />;
};
export default RedirectIfLoggedIn;
