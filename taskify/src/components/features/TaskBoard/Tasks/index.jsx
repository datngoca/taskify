import classNames from "classnames/bind";
import {
  DndContext,
  DragOverlay,
  closestCorners,
  useSensors,
  useSensor,
  PointerSensor,
} from "@dnd-kit/core";

import styles from "./Tasks.module.scss";
import TaskInput from "../TaskInput";
import TaskItem from "../TaskItem";
import ColumnTasks from "../ColumnTasks";

// Import Custom Hook và Constants
import { useTask } from "../useTask";
import { COLUMNS } from "../constants";

const cx = classNames.bind(styles);

const Tasks = () => {
  const { tasks, activeId, activeTask, handleDragStart, handleDragEnd } =
    useTask();

  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 5 } })
  );
  return (
    <div className={cx("container")}>
      <TaskInput />
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

export default Tasks;
