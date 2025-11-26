import { useState } from "react";
import classNames from "classnames/bind";

import styles from "./TaskEdit.module.scss";
import Button from "../../UI/Button/Button";
import Input from "../../UI/Input/Input";

const cx = classNames.bind(styles);

const TaskEdit = ({ task, onSave, onCancel }) => {
  const [editedValue, setEditedValue] = useState(task.name);

  const handleSave = () => {
    onSave(task.id, { fieldName: "name", value: editedValue });
    onCancel();
  };

  return (
    <div className={cx("edit-task")}>
      <h2>Edit Task</h2>
      <Input
        type="text"
        defaultValue={task.name}
        onChange={(e) => setEditedValue(e.target.value)}
      />
      <Button onClick={handleSave}>Save</Button>
      <Button onClick={() => onCancel}>Cancel</Button>
    </div>
  );
};

export default TaskEdit;
