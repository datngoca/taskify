import classNames from "classnames/bind";
import { DndContext, DragOverlay, closestCorners } from "@dnd-kit/core";

import styles from "./Task.module.scss";
import TaskInput from "./TaskInput/TaskInput";
import TaskItem from "./TaskItem/TaskItem";
import ColumnTasks from "./ColumnTasks/ColumnTasks";

// Import Custom Hook và Constants
import { useTaskBoard } from "../../hooks/useTaskBoard";
import { COLUMNS } from "./constants";

const cx = classNames.bind(styles);

const Task = () => {
  // Lấy toàn bộ logic từ Custom Hook
  const {
    tasks,
    activeId,
    activeTask,
    sensors,
    handleAddTask,
    handleDeleteTask,
    handleUpdateTask,
    handleDragStart,
    handleDragEnd,
  } = useTaskBoard();

  return (
    <div className={cx("container")}>
      <TaskInput onAddTask={handleAddTask} />

      <DndContext
        sensors={sensors}
        collisionDetection={closestCorners}
        onDragStart={handleDragStart}
        onDragEnd={handleDragEnd}
      >
        <div className={cx("board")}>
          {COLUMNS.map((col) => (
            <ColumnTasks
              key={col.id}
              col={col}
              tasks={tasks.filter((t) => t.status === col.id)}
              handleDeleteTask={handleDeleteTask}
              handleUpdateTask={handleUpdateTask}
            />
          ))}
        </div>

        <DragOverlay>
          {activeId && activeTask ? (
            <TaskItem task={activeTask} isOverlay={true} />
          ) : null}
        </DragOverlay>
      </DndContext>
    </div>
  );
};

export default Task;
