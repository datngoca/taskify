import classNames from "classnames/bind";
import { NavLink } from "react-router-dom";
import {
  FiGrid,
  FiHome,
  FiUser,
  FiLogOut,
  FiSettings,
  FiLayout, // For Templates
} from "react-icons/fi";

import styles from "./SideBar.module.scss";

const cx = classNames.bind(styles);

const WORKSPACE_ITEMS = [
  {
    icon: <FiGrid />,
    title: "Boards",
    to: "/boards",
  },
  {
    icon: <FiLayout />,
    title: "Templates",
    to: "/templates", // Placeholder path
  },
  {
    icon: <FiHome />,
    title: "Home",
    to: "/",
  },
];

const MENU_ITEMS = [
  {
    icon: <FiUser />,
    title: "Profile",
    to: "/profile",
  },
  {
    icon: <FiSettings />,
    title: "Settings",
    to: "/settings",
  },
];

function SideBar() {
  return (
    <div className={cx("sidebar")}>
      <nav className={cx("nav")}>
        <p className={cx("nav-title")}>WORKSPACES</p>
        {WORKSPACE_ITEMS.map((item, index) => (
          <NavLink
            key={index}
            to={item.to}
            className={(nav) => cx("nav-item", { active: nav.isActive })}
          >
            {item.icon}
            <span>{item.title}</span>
          </NavLink>
        ))}
        <div className={cx("divider")} /> {/* The requested dash separator */}
        <p className={cx("nav-title")}>MENU</p>
        {MENU_ITEMS.map((item, index) => (
          <NavLink
            key={index}
            to={item.to}
            className={(nav) => cx("nav-item", { active: nav.isActive })}
          >
            {item.icon}
            <span>{item.title}</span>
          </NavLink>
        ))}
      </nav>

      <button className={cx("logout-btn")}>
        <FiLogOut />
        <span>Logout</span>
      </button>
    </div>
  );
}

export default SideBar;
