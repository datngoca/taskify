import { LoginPage, RegisterPage } from "@/features/auth";
import { BoardPage, BoardDetailPage } from "@/features/board";
import Home from "@/pages/Home/Home.jsx";
import NotFoundPage from "@/pages/NotFound/NotFoundPage.jsx";

const publicRoutes = [
  {
    path: "/login",
    component: LoginPage,
  },
  {
    path: "/register", // Add Register route
    component: RegisterPage,
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
    component: BoardPage,
  },

  {
    path: "/board/:id",
    component: BoardDetailPage,
  },
];
export { publicRoutes, privateRoutes };
