import { BrowserRouter, Routes, Route } from "react-router-dom";
import { publicRoutes, privateRoutes } from "./routes/route";
import { AuthProvider } from "./context/AuthContext.jsx";
import PrivateRoute from "./routes/PrivateRoute.jsx";
import RedirectIfLoggedIn from "./routes/RedirectIfLoggedIn.jsx";
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          {publicRoutes.map((route, index) => {
            const Page = route.component;
            return (
              <Route
                key={index}
                path={route.path}
                element={
                  <RedirectIfLoggedIn>
                    <Page />
                  </RedirectIfLoggedIn>
                }
              />
            );
          })}
          {/* Private routes can be added here similarly using PrivateRoute component */}
          {privateRoutes.map((route, index) => {
            const Page = route.component;
            return (
              <Route
                key={index}
                path={route.path}
                element={
                  <PrivateRoute>
                    <Page />
                  </PrivateRoute>
                }
              />
            );
          })}
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
