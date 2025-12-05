import classNames from "classnames/bind";
import styles from "./TaskBoard.module.scss";
import { TaskProvider } from "./context/TaskContext";
import Tasks from "./Tasks";

const cx = classNames.bind(styles);

const TaskBoard = () => {
  return (
    <TaskProvider>
      <div className={cx("wrapper")}>
        <Tasks />
      </div>
    </TaskProvider>
  );
};

export default TaskBoard;
