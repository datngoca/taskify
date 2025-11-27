import { useState } from "react";
import { FaCheck } from "react-icons/fa";
import classNames from "classnames/bind";

import TaskEdit from "../TaskEdit/TaskEdit.jsx";
import styles from "./TaskItem.module.scss";
import Button from "../../UI/Button/Button.jsx";

const cx = classNames.bind(styles);

const TaskItem = ({ task, onDeleteTask, onSaveTask }) => {
  const [status, setStatus] = useState(task.completed);
  const [editingTask, setEditingTask] = useState(null);

  const handleStatusChange = () => {
    setStatus(!status);
    onSaveTask(task.id, { fieldName: "completed", value: !status });
  };
  return (
    <>
      <div className={cx("taskItem")}>
        <div className={cx("leftContent")}>
          <input
            onChange={handleStatusChange}
            type="checkbox"
            checked={status}
          />
          <span className={styles.taskText}>{task.name}</span>
          <span className={cx("status")}>
            {task.completed ? <FaCheck className={cx("icon")} /> : ""}
          </span>
        </div>
        <div className={cx("actions")}>
          {/* Nút Edit dùng style secondary */}
          <Button secondary onClick={() => setEditingTask(task)}>
            Edit
          </Button>

          {/* Nút Delete dùng style danger */}
          <Button danger onClick={() => onDeleteTask(task.id)}>
            Delete
          </Button>
        </div>
      </div>
      {/* RENDER MODAL CÓ ĐIỀU KIỆN */}
      {/* Nếu editingTask khác null thì hiện Modal */}
      {editingTask && (
        <TaskEdit
          task={editingTask}
          onSave={onSaveTask}
          onCancel={() => setEditingTask(null)}
        />
      )}
    </>
  );
};
export default TaskItem;
