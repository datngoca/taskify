import React, { memo, useState } from "react";
import {
  SortableContext,
  useSortable,
  verticalListSortingStrategy,
} from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import classNames from "classnames/bind";
import TaskCard from "../TaskCard";
import styles from "./Column.module.scss";
import { useBoardData } from "@/features/board/hooks";
import Input from "@/components/Input";
import Button from "@/components/Button";
import { MdCancel } from "react-icons/md";

const cx = classNames.bind(styles);

const Column = ({ column }) => {
  const { createNewCard } = useBoardData(); // Lấy hàm tạo task từ Context

  const [isAddTask, setIsAddTask] = useState(false);
  const [titleTask, setTitleTask] = useState("");

  // 1. Setup Dnd cho chính Cột này (Kéo cột)
  const {
    attributes,
    listeners,
    setNodeRef,
    transform,
    transition,
    isDragging,
  } = useSortable({
    id: column.id,
    data: { ...column, type: "COLUMN" },
  });
  const handleAddTask = () => {
    if (titleTask != "") createNewCard(column.id, titleTask);
    setIsAddTask(false);
  };

  const dndStyle = {
    transform: CSS.Translate.toString(transform),
    transition,
  };

  // Lấy mảng ID tasks cho SortableContext
  const taskIds = column.tasks?.map((t) => t.id) || [];

  return (
    <div
      ref={setNodeRef}
      style={dndStyle}
      className={cx("column", { isDragging })}
    >
      {/* Header: Nơi nắm để kéo cột */}
      <div className={cx("header")} {...attributes} {...listeners}>
        {column.title}
        <span>...</span>
      </div>

      {/* Body: Sortable Tasks */}
      <div className={cx("taskList")}>
        <SortableContext items={taskIds} strategy={verticalListSortingStrategy}>
          {column.tasks?.map((task) => (
            <TaskCard key={task.id} task={task} />
          ))}
        </SortableContext>
      </div>

      {/* Footer: Add new card */}
      {!isAddTask ? (
        <div className={cx("footer")}>
          <button onClick={() => setIsAddTask(true)}>+ Add a card</button>
        </div>
      ) : (
        <div className={cx("addTaskArea")}>
          <Input
            onChange={(e) => setTitleTask(e.target.value)}
            placeholder="Enter name task..."
          />
          <div className={cx("actions")}>
            <Button primary onClick={handleAddTask}>
              Add Column
            </Button>
            <button
              className={cx("cancelBtn")}
              onClick={() => setIsAddTask(false)}
            >
              <MdCancel />
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default memo(Column);
