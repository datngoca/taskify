import { BrowserRouter, Routes, Route, Outlet } from "react-router-dom";
import { publicRoutes, privateRoutes } from "./routes/route";
import { AuthProvider } from "./context/AuthContext.jsx";
import PrivateRoute from "./routes/PrivateRoute.jsx";
import RedirectIfLoggedIn from "./routes/RedirectIfLoggedIn.jsx";
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route
            element={
              <RedirectIfLoggedIn>
                <Outlet />
              </RedirectIfLoggedIn>
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
              <PrivateRoute>
                <Outlet />
              </PrivateRoute>
            }
          >
            {privateRoutes.map((route, index) => {
              const Page = route.component;
              return <Route key={index} path={route.path} element={<Page />} />;
            })}
          </Route>
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
