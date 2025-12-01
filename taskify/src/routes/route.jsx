import Home from "../pages/Home/Home.jsx";
import Login from "../pages/Auth/Login.jsx";
import NotFoundPage from "../pages/NotFound/NotFoundPage.jsx";

const publicRoutes = [
  {
    path: "/login",
    component: Login,
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
