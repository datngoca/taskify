import { useAuth } from "@/features/auth";
import classNames from "classnames/bind";
import styles from "./Header.module.scss";
import images from "@/assets/img";
import Button from "@/components/Button";
const cx = classNames.bind(styles);
const Header = () => {
  const { logout, isAuthenticated } = useAuth();

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
      {isAuthenticated ? (
        <Button type="secondary" className={cx("btn-login")} onClick={logout}>
          Logout
        </Button>
      ) : (
        <div>
          <Button type="secondary" className={cx("btn-login")}>
            Login
          </Button>
        </div>
      )}
    </header>
  );
};
export default Header;
