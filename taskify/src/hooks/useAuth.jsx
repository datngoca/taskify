import { useContext } from "react";
import { AuthContext } from "@/contexts/AuthContext.jsx";
const useAuth = () => {
  const context = useContext(AuthContext);
  // Kiểm tra an toàn: Nếu dùng useAuth bên ngoài <AuthProvider> thì báo lỗi ngay
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
export { useAuth };
