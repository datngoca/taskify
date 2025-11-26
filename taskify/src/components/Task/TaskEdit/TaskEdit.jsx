import { useState } from "react";
import classNames from "classnames/bind";
import styles from "./TaskEdit.module.scss";
const cx = classNames.bind(styles);

const TaskEdit = ({ task, onSave, onCancel }) => {
  const [editedValue, setEditedValue] = useState(task.name);
  const handleSave = () => {
    onSave(task.id, editedValue);
    onCancel();
  };
  return (
    <div className={cx("edit-task")}>
      <h2>Edit Task</h2>
      <input
        type="text"
        defaultValue={task.name}
        onChange={(e) => setEditedValue(e.target.value)}
      />
      <button onClick={handleSave}>Save</button>
      <button onClick={() => onCancel}>Cancel</button>
    </div>
  );
};

export default TaskEdit;
