import { Navigate } from "react-router-dom";
import { useAuth } from "@/features/auth";
const PrivateRoute = ({ children }) => {
  const { isAuthenticated, isInitialized } = useAuth();
  if (!isInitialized) {
    return <div className="loading-spinner">Loading bao ve</div>;
  }
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" replace />;
};
export default PrivateRoute;
