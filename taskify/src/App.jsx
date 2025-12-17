import { BrowserRouter } from "react-router-dom";

import AppRoute from "./routes/AppRoute.jsx";
function App() {
  return (
    <BrowserRouter>
        <AppRoute />
    </BrowserRouter>
  );
}

export default App;
