import { useState, useContext } from "react";
import { FaCheck, FaEdit, FaTrash } from "react-icons/fa";
import classNames from "classnames/bind";

import TaskEdit from "../TaskEdit/TaskEdit.jsx";
import styles from "./TaskItem.module.scss";
import Button from "../../UI/Button/Button.jsx";
import { TaskContext } from "../Task.jsx";
const cx = classNames.bind(styles);

const TaskItem = ({ task, isOverlay }) => {
  const { handleDeleteTask, handleUpdateTask } = useContext(TaskContext);
  const [status, setStatus] = useState(task.completed);
  const [editingTask, setEditingTask] = useState(null);

  const handleStatusChange = () => {
    setStatus(!status);
    handleUpdateTask(task.id, { fieldName: "completed", value: !status });
  };
  return (
    <>
      <div
        className={cx("taskItem")}
        style={{
          // Nếu là Overlay (cái đang bay) thì thêm bóng đổ đậm hơn, scale to lên xíu cho đẹp
          cursor: "grabbing",
          boxShadow: isOverlay ? "0 5px 15px rgba(0,0,0,0.25)" : undefined,
          transform: isOverlay ? "scale(1.05)" : undefined,
        }}
      >
        <div className={cx("leftContent")}>
          {/* <input
            // onChange={handleStatusChange}
            type="checkbox"
            checked={status}
          /> */}
          <span className={styles.taskText}>{task.title}</span>
          <span className={cx("status")}>
            {task.completed ? <FaCheck className={cx("icon")} /> : ""}
          </span>
        </div>
        <div className={cx("actions")}>
          {/* Nút Edit dùng style secondary */}
          <Button
            secondary
            onClick={() => setEditingTask(task)}
            rightIcon={<FaEdit />}
          >
            Edit
          </Button>

          {/* Nút Delete dùng style danger */}
          <Button
            danger
            onClick={() => handleDeleteTask(task.id)}
            rightIcon={<FaTrash />}
          >
            Delete
          </Button>
        </div>
      </div>
      {/* RENDER MODAL CÓ ĐIỀU KIỆN */}
      {/* Nếu editingTask khác null thì hiện Modal */}
      {editingTask && (
        <TaskEdit task={task} onCancel={() => setEditingTask(null)} />
      )}
    </>
  );
};
export default TaskItem;
