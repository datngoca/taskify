import { useState } from "react";
import { FaCheck } from "react-icons/fa";
import classNames from "classnames/bind";

import TaskEdit from "../TaskEdit/TaskEdit.jsx";
import styles from "./TaskItem.module.scss";
import Button from "../../UI/Button/Button.jsx";

const cx = classNames.bind(styles);

const TaskItem = ({ task, onDeleteTask, onSaveTask }) => {
  const [status, setStatus] = useState(task.completed);
  const [isEditing, setIsEditing] = useState(false);

  const handleStatusChange = () => {
    setStatus(!status);
    onSaveTask(task.id, { fieldName: "completed", value: !status });
  };

  return (
    <>
      <div className={cx("task-item")}>
        <div className={cx("checkbox")}>
          <input
            onChange={handleStatusChange}
            type="checkbox"
            name=""
            id=""
            checked={status}
          />
        </div>
        <div className={cx("task-content")}>
          <div className={cx("task-info")}>
            {!isEditing ? (
              <h2 className={cx("task-name")}>{task.name}</h2>
            ) : (
              <TaskEdit
                task={task}
                onSave={onSaveTask}
                onCancel={() => setIsEditing(false)}
              />
            )}
            <span className={cx("status")}>
              {task.completed ? <FaCheck className={cx("icon")} /> : ""}
            </span>
          </div>
          <div className={cx("actions")}>
            <Button onClick={() => setIsEditing(true)}>Edit</Button>
            <Button onClick={() => onDeleteTask(task.id)}>Delete</Button>
          </div>
        </div>
      </div>
    </>
  );
};
export default TaskItem;
