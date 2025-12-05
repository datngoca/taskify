import { useState } from "react";
import classNames from "classnames/bind";

import styles from "./TaskEdit.module.scss";
import Button from "@/components/common/Button";
import Input from "@/components/common/Input";
import { useTask } from "../useTask";
const cx = classNames.bind(styles);

const TaskEdit = ({ task, onCancel }) => {
  const { tasks, handleUpdateTask } = useTask();
  const [editedValue, setEditedValue] = useState(task.title);

  const handleSave = () => {
    const editedTask = tasks.find((t) => t.id === task.id);
    editedTask.title = editedValue;
    handleUpdateTask(editedTask);
    onCancel();
  };
  return (
    <div className={cx("overlay")}>
      {/* stopPropagation để click vào modal không bị đóng modal */}
      <div className={cx("modal")}>
        <h3 className={cx("title")}>Edit Task</h3>

        <Input
          type="text"
          className={cx("input")}
          onChange={(e) => setEditedValue(e.target.value)}
          autoFocus
          onKeyDown={(e) => e.key === "Enter" && handleSave()}
          value={editedValue}
        />

        <div className={cx("buttonGroup")}>
          <Button secondary onClick={onCancel}>
            Cancel
          </Button>
          <Button primary onClick={handleSave}>
            Save Changes
          </Button>
        </div>
      </div>
    </div>
  );
};

export default TaskEdit;
