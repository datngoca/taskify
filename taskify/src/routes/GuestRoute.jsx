import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "@/features/auth";
const GuestRoute = () => {
  const { isAuthenticated, isInitialized } = useAuth();
  if (!isInitialized) {
    return <div className="loading-spinner">Loading khong cho user tro ve khach</div>;
  }
  return isAuthenticated ?  <Navigate to="/" replace />:<Outlet />;
};
export default GuestRoute;
