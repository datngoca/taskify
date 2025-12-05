import classNames from "classnames/bind";
import styles from "./MainLayout.module.scss";

import Header from "@/components/features/Header";
import SideBar from "@/components/features/SideBar";

const cx = classNames.bind(styles);

const MainLayout = ({ children }) => {
  return (
    <div className={cx("mainLayout")}>
      <Header />
      <div className={cx("main-content-area")}>
        <SideBar />
        <div className={cx("contentWrapper")}>
          <div className={cx("container")}>{children}</div>
        </div>
      </div>
    </div>
  );
};

export default MainLayout;
