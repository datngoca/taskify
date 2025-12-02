import classNames from "classnames/bind";
import {
  SortableContext,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";

import styles from "./TaskList.module.scss";
import TaskItem from "../TaskItem/TaskItem.jsx";
import SortableItem from "../SortableItem/index.jsx";

const cx = classNames.bind(styles);

const TaskList = ({ tasks }) => {
  return (
    <SortableContext
      items={tasks} // Cần truyền mảng các ID
      strategy={verticalListSortingStrategy}
    >
      <div className={cx("taskList")}>
        {tasks?.length === 0 ? (
          <p className={cx("no-tasks")}>No tasks available</p>
        ) : (
          tasks?.map((task) => (
            <SortableItem key={task.id} id={task.id}>
              <TaskItem task={task} />
            </SortableItem>
          ))
        )}
      </div>
    </SortableContext>
  );
};

export default TaskList;
