import Home from "@/pages/Home/Home.jsx";
import Login from "@/pages/Auth/Login.jsx";
import Register from "@/pages/Auth/Register.jsx"; // Import Register component
import NotFoundPage from "@/pages/NotFound/NotFoundPage.jsx";
import BoardsPage from "@/pages/Boards";
import BoardPage from "@/pages/Board";

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
  {
    path: "/boards",
    component: BoardsPage,
  },
  {
    path: "/board/:id",
    component: BoardPage,
  },
];
export { publicRoutes, privateRoutes };
