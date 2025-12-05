import classNames from "classnames/bind";
import { useNavigate } from "react-router-dom";

import styles from "./Boards.module.scss";
import Button from "@/components/common/Button";

const cx = classNames.bind(styles);
// Dữ liệu giả (Sau này sẽ gọi API lấy từ DB về)
const mockBoards = [
  { id: 1, title: "Dự án Marketing" },
  { id: 2, title: "Học lập trình Web" },
  { id: 3, title: "Việc nhà" },
];
const Boards = () => {
  const navigate = useNavigate();
  return (
    <div className={styles.container}>
      <div className={cx("grid")}>
        {/* Render danh sách board */}
        {mockBoards.map((board) => (
          <div
            key={board.id}
            className={cx("boardCard")}
            onClick={() => navigate(`/board/${board.id}`)} // Chuyển hướng sang trang Kanban
          >
            <h3>{board.title}</h3>
            <span style={{ fontSize: "0.8rem", color: "#666" }}>
              Click to open
            </span>
          </div>
        ))}

        {/* Nút tạo board mới */}
        <Button className={cx("boardCard")}>+ Create New Board</Button>
      </div>
    </div>
  );
};
export default Boards;
