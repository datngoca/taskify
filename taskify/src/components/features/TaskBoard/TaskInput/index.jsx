import { useState } from "react";
import classNames from "classnames/bind";

import styles from "./TaskInput.module.scss";
import Button from "@/components/common/Button";
import Input from "@/components/common/Input";

import { useTask } from "../useTask";
const cx = classNames.bind(styles);

const TaskInput = () => {
  const { handleCreateTask } = useTask();
  const [inputValue, setInputValue] = useState("");

  return (
    <>
      <div className={cx("inputGroup")}>
        <Input
          onChange={(e) => setInputValue(e.target.value)}
          type="text"
          placeholder="Enter your task..."
        />
        <Button primary onClick={() => handleCreateTask({ title: inputValue })}>
          Add
        </Button>
      </div>
    </>
  );
};

export default TaskInput;
