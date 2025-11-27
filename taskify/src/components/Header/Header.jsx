import { useState } from "react";

import classNames from "classnames/bind";
import styles from "./Header.module.scss";
import images from "../../assets/img/index.jsx";
import Button from "../UI/Button/Button.jsx";
const cx = classNames.bind(styles);
const Header = () => {
  const [pageActive, setPageActive] = useState("Home");

  return (
    <header>
      <a href="" className={cx("logo")}>
        <img src={images.logo} alt="Logo" />
      </a>
      <div className={cx("header-right")}>
        <a href="" className={cx("active")}>
          Home
        </a>
        <a href="">About</a>
      </div>
      <div>
        <Button type="secondary" className={cx("btn-login")}>
          Login
        </Button>
      </div>
    </header>
  );
};
export default Header;
