import classNames from "classnames/bind";
import styles from "./Board.module.scss";
import HeaderOnly from "@/layouts/HeaderOnly";

const cx = classNames.bind(styles);
import BoardCanvas from "./BoardCanvas";
import BoardHeader from "./BoardHeader/BoardHeader";
const Board = () => {
  return (
    <div className={cx("containerBoard")}>
      <HeaderOnly>
        <div className={cx("header")}>
          <BoardHeader />
        </div>
        <div className={cx("content")}>
          <BoardCanvas />
        </div>
      </HeaderOnly>
    </div>
  );
};
export default Board;
