import classNames from "classnames/bind";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

import styles from "./BoardList.module.scss";
import Button from "@/components/Button";
import CreateBoardModal from "../CreateBoardModal";

import { useBoardList } from "@/features/board/hooks";
import { MdDelete } from "react-icons/md";

const cx = classNames.bind(styles);

const Boards = () => {
  const { boards } = useBoardList();
  const [isCreate, setIsCreate] = useState(false);
  const navigate = useNavigate();
  return (
    <div className={cx("container")}>
      <div className={cx("grid")}>
        {/* Render danh sách board */}
        {boards.map((board) => (
          <div
            key={board.id}
            className={cx("boardCard")}
            onClick={() => navigate(`/board/${board.id}`)} // Chuyển hướng sang trang Kanban
          >
            <div className={cx("left")}>
              <h3>{board.title}</h3>
              <span style={{ fontSize: "0.8rem", color: "#666" }}>
                Click to open
              </span>
            </div>
            <Button danger className={cx("right")}>
              <MdDelete />
            </Button>
          </div>
        ))}

        {/* Nút tạo board mới */}
        <Button className={cx("boardCard")} onClick={() => setIsCreate(true)}>
          + Create New Board
        </Button>
      </div>
      {/* SỬA LẠI ĐOẠN NÀY: Chỉ hiển thị khi isCreate = true */}
      {isCreate && (
        <CreateBoardModal
          onClose={() => setIsCreate(false)} // Truyền hàm đóng xuống component con
        />
      )}
    </div>
  );
};
export default Boards;
