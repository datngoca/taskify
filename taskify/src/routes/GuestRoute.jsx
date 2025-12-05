import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/hooks/useAuth.jsx";
const GuestRoute = () => {
  const { isAuthenticated, isInitialized } = useAuth();
  if (!isInitialized) {
    return <div className="loading-spinner">Loading...</div>;
  }
  return isAuthenticated ?  <Navigate to="/" replace />:<Outlet />;
};
export default GuestRoute;
