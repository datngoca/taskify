import { useState } from "react";
import classNames from "classnames/bind";
import styles from "./TaskInput.module.scss";

const cx = classNames.bind(styles);

const TaskInput = ({ onAddTask }) => {
  const [inputValue, setInputValue] = useState("");

  const handleAddTask = () => {
    if (inputValue.trim() !== "") {
      const newTask = {
        name: inputValue,
        completed: false,
      };

      onAddTask(newTask);
      setInputValue("");
    }
  };
  return (
    <>
      <div className={cx("task-input")}>
        <input
          onChange={(e) => setInputValue(e.target.value)}
          type="text"
          placeholder="Enter your task..."
        />
        <button onClick={handleAddTask}>Add</button>
      </div>
    </>
  );
};

export default TaskInput;
