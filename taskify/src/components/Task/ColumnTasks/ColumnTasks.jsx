import {
  SortableContext,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { useDroppable } from "@dnd-kit/core";
import classNames from "classnames/bind";

import styles from "./ColumnTasks.module.scss";
import TaskList from "../TaskList/TaskList";

const cx = classNames.bind(styles);
const ColumnTasks = ({ col, tasks, handleDeleteTask, handleUpdateTask }) => {
  const { setNodeRef } = useDroppable({
    id: col.id, // ID của cột: 'todo', 'doing', 'done'
  });
  return (
    <div ref={setNodeRef} className={cx("column")}>
      <h2 className={cx("columnTitle")}>
        {col.title}
        <span>{tasks.length}</span>
      </h2>

      <SortableContext
        items={tasks.map((t) => t.id)}
        strategy={verticalListSortingStrategy}
      >
        <TaskList
          tasks={tasks}
          onDeleteTask={handleDeleteTask}
          onSaveTask={handleUpdateTask}
          // onCheckTask={handleUpdateTask}
        />
      </SortableContext>
    </div>
  );
};
export default ColumnTasks;
