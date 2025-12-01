import classNames from "classnames/bind";
import styles from "./NotFound.module.scss";

const cx = classNames.bind(styles);
const NotFound = () => {
  return (
    <div className={cx("notFoundContainer")}>
      <h1>404</h1>
      <h2>Oops! Trang bạn tìm kiếm không tồn tại.</h2>
      <p>Có vẻ như đường dẫn bị hỏng hoặc trang đã bị xóa.</p>
    </div>
  );
};

export default NotFound;
