import classNames from "classnames/bind";
import styles from "./BoardHeader.module.scss";

const cx = classNames.bind(styles);
const BoardHeader = () => {
  return <div className={cx("boardHeader")}>Hello</div>;
};
export default BoardHeader;
