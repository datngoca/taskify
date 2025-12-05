import { useState, useEffect } from "react";
import { Outlet } from "react-router-dom";

import { useAuth } from "@/hooks/useAuth";
import authService from "@/services/authService";

const PersistLogin = () => {
  const [isLoading, setIsLoading] = useState(true);
  const { initialize } = useAuth();

  useEffect(() => {
    /**
     * Verify refresh token and initialize user state
     * @returns {Promise<void>} Promise resolve when finished verifying refresh token
     */
    const verifyRefreshToken = async () => {
      const token = localStorage.getItem("token");
      try {
        if (token) {
          // Gọi API lấy info user.
          // Nếu token hết hạn, axiosClient bên kia sẽ TỰ ĐỘNG refresh.
          const user = await authService.getCurrentUser();
          initialize(true, user);
        } else {
          initialize(false, null);
        }
      } catch (err) {
        // Nếu axiosClient đã refresh mà vẫn lỗi -> Nó đã xóa token & redirect rồi
        // Ở đây ta chỉ cần update state về false cho chắc chắn
        console.error("PersistLogin error:", err);
        initialize(false, null);
      } finally {
        setIsLoading(false);
      }
    };

    // Nếu chưa có token thì không cần loading làm gì (hoặc loading rất nhanh)
    // Nhưng ta vẫn cần set initialized = true
    const token = localStorage.getItem("token");
    if (!token) {
      initialize(false, null);
      setIsLoading(false);
    } else {
      verifyRefreshToken();
    }
  }, []);

  return isLoading ? <p>Loading...</p> : <Outlet />;
};

export default PersistLogin;
