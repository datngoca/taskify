import classNames from "classnames/bind";
import styles from "./TaskList.module.scss";

import TaskItem from "../TaskItem/TaskItem.jsx";

const cx = classNames.bind(styles);

const TaskList = ({ tasks, onDeleteTask, onSaveTask }) => {
  return (
    <>
      <div className={cx("task-list")}>
        {tasks?.length === 0 ? (
          <p className={cx("no-tasks")}>No tasks available</p>
        ) : (
          tasks?.map((task, index) => (
            <TaskItem
              key={index}
              task={task}
              onDeleteTask={onDeleteTask}
              onSaveTask={onSaveTask}
            />
          ))
        )}
      </div>
    </>
  );
};

export default TaskList;
