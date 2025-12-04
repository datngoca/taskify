import { useState } from "react";
import classNames from "classnames/bind";
import styles from "./TaskFilter.module.scss";
import Button from "../../common/Button";

const cx = classNames.bind(styles);

const TaskFilter = ({ onFilterTask }) => {
  const options = ["All", "Completed", "Incomplete"];
  const [selectedOption, setSelectedOption] = useState("All");

  const handleFilterChange = (option) => {
    setSelectedOption(option);
    onFilterTask(option);
  };
  return (
    <div className={cx("filterGroup")}>
      {options.map((option, index) => (
        <Button
          key={index}
          primary={selectedOption === option}
          secondary={selectedOption !== option}
          onClick={() => handleFilterChange(option)}
        >
          {option}
        </Button>
      ))}
    </div>
  );
};
export default TaskFilter;
