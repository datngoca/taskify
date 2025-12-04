import { useState } from "react";
import classNames from "classnames/bind";

import styles from "./TaskInput.module.scss";
import Button from "../../common/Button";
import Input from "../../common/Input";

const cx = classNames.bind(styles);

const TaskInput = ({ onAddTask }) => {
  const [inputValue, setInputValue] = useState("");

  const handleAddTask = () => {
    if (inputValue.trim() !== "") {
      const newTask = {
        title: inputValue,
      };

      onAddTask(newTask);
      setInputValue("");
    }
  };
  return (
    <>
      <div className={cx("inputGroup")}>
        <Input
          onChange={(e) => setInputValue(e.target.value)}
          type="text"
          placeholder="Enter your task..."
        />
        <Button primary onClick={handleAddTask}>
          Add
        </Button>
      </div>
    </>
  );
};

export default TaskInput;
