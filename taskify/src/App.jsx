import { BrowserRouter } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext.jsx";

import AppRoute from "./routes/AppRoute.jsx";
function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoute />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
