import { useParams } from "react-router-dom";
import classNames from "classnames/bind";

import styles from "./TaskBoard.module.scss";
import { TaskProvider } from "./context/TaskContext";
import Tasks from "./Tasks";
import Button from "@/components/common/Button";
import { FaArrowLeft } from "react-icons/fa";
const cx = classNames.bind(styles);

const TaskBoard = () => {
  const { id } = useParams();
  return (
    <TaskProvider>
      <div className={cx("container")}>
        {/* Thêm nút Back */}
        <Button secondary to="/boards" leftIcon={<FaArrowLeft />}>
          Back to Boards
        </Button>
        <h1 className={cx("title")}>
          Board #{id} {/* Hiển thị ID tạm thời */}
        </h1>
        <Tasks />
      </div>
    </TaskProvider>
  );
};

export default TaskBoard;
