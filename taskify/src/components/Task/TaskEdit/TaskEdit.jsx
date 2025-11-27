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
    <div className={cx("overlay")}>
      {/* stopPropagation để click vào modal không bị đóng modal */}
      <div className={cx("modal")}>
        <h3 className={cx("title")}>Edit Task</h3>

        <Input
          type="text"
          className={cx("input")}
          defaultValue={editedValue}
          onChange={(e) => setEditedValue(e.target.value)}
          autoFocus
          onKeyDown={(e) => e.key === "Enter" && handleSave()}
        />

        <div className={cx("buttonGroup")}>
          <Button secondary onClick={onCancel} >
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
