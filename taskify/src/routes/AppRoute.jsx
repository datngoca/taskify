import { Routes, Route, Outlet } from "react-router-dom";

import PersistLogin from "@/routes/PersistLogin.jsx";
import ProtectedRoute from "./ProtectedRoute.jsx";
import GuestRoute from "./GuestRoute.jsx";
import { publicRoutes, privateRoutes } from "./route";

const AppRoute = () => {
  return (
    <Routes>
      <Route element={<PersistLogin />}>
        <Route
          element={
            <GuestRoute>
              <Outlet />
            </GuestRoute>
          }
        >
          {publicRoutes.map((route, index) => {
            const Page = route.component;
            return <Route key={index} path={route.path} element={<Page />} />;
          })}
        </Route>

        {/* Private routes can be added here similarly using PrivateRoute component */}
        <Route
          element={
            <ProtectedRoute>
              <Outlet />
            </ProtectedRoute>
          }
        >
          {privateRoutes.map((route, index) => {
            const Page = route.component;
            return <Route key={index} path={route.path} element={<Page />} />;
          })}
        </Route>
      </Route>
    </Routes>
  );
};

export default AppRoute;
