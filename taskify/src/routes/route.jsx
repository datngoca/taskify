import Home from "@/pages/Home/Home.jsx";
import Login from "@/pages/Auth/Login.jsx";
import Register from "@/pages/Auth/Register.jsx"; // Import Register component
import NotFoundPage from "@/pages/NotFound/NotFoundPage.jsx";

const publicRoutes = [
  {
    path: "/login",
    component: Login,
  },
  {
    path: "/register", // Add Register route
    component: Register,
  },
  {
    path: "*",
    component: NotFoundPage,
  },
];
const privateRoutes = [
  {
    path: "/",
    component: Home,
  },
];
export { publicRoutes, privateRoutes };
