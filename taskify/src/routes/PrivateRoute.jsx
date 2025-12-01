import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../hooks/useAuth.jsx";
const PrivateRoute = ({ children }) => {
  const { isAuthenticated, isInitialized } = useAuth();
  if (!isInitialized) {
    return <div className="loading-spinner">Loading...</div>;
  }
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
};
export default PrivateRoute;
