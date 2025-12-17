import React, { useState, useCallback } from "react";
import {
  DndContext,
  DragOverlay,
  useSensor,
  useSensors,
  MouseSensor,
  TouchSensor,
  closestCorners,
} from "@dnd-kit/core";
import {
  SortableContext,
  horizontalListSortingStrategy,
} from "@dnd-kit/sortable";
import classNames from "classnames/bind";

import { useBoardData } from "../../../../hooks";
import Column from "./Column";
import TaskCard from "./TaskCard";
import styles from "./BoardCanvas.module.scss";
import Button from "@/components/Button";
import Input from "@/components/Input";

import { MdCancel } from "react-icons/md";

const cx = classNames.bind(styles);

const BoardCanvas = () => {
  const { columns, moveColumns, moveCards, createNewColumn } = useBoardData();

  const [activeDragItem, setActiveDragItem] = useState(null);

  const [isAddColumn, setIsAddColumn] = useState(false);
  const [titleColum, setTitleColumn] = useState("");

  // 1. Setup Sensors (Chuột & Cảm ứng)
  const sensors = useSensors(
    useSensor(MouseSensor, { activationConstraint: { distance: 10 } }),
    useSensor(TouchSensor, {
      activationConstraint: { delay: 250, tolerance: 5 },
    })
  );

  const handleAddColumn = () => {
    createNewColumn(titleColum);
    setIsAddColumn(false);
  };

  // 2. Event Handlers
  const handleDragStart = (event) => {
    console.log("event Start", event);
    setActiveDragItem(event.active.data.current);
  };

  const handleDragEnd = useCallback(
    (event) => {
      console.log("event End", event);

      const { active, over } = event;
      if (!over) {
        setActiveDragItem(null);
        return;
      }

      const type = active.data.current?.type;

      if (type === "COLUMN") {
        if (active.id !== over.id) {
          moveColumns({ active, over });
        }
      }
      // Logic moveCard (task) thường phức tạp hơn (xử lý ở DragOver hoặc DragEnd)
      // Tạm thời gọi hàm moveCard giả định
      else if (type === "TASK") {
        if (active.id !== over.id) moveCards({ active, over });
      }

      setActiveDragItem(null);
    },
    [moveColumns, moveCards]
  );

  const columnIds = columns.map((c) => c.id);

  return (
    <DndContext
      sensors={sensors}
      collisionDetection={closestCorners}
      onDragStart={handleDragStart}
      onDragEnd={handleDragEnd}
    >
      <div className={cx("boardContent")}>
        {/* Sortable cho Columns (Ngang) */}
        <SortableContext
          items={columnIds}
          strategy={horizontalListSortingStrategy}
        >
          {columns.map((col) => (
            <Column key={col.id} column={col} />
          ))}
        </SortableContext>

        {/* Button Add Column */}
        {!isAddColumn ? (
          <div
            className={cx("addColumnBtn")}
            onClick={() => setIsAddColumn(true)}
          >
            + Add another list
          </div>
        ) : (
          <div className={cx("addColumnArea")}>
            <Input
              onChange={(e) => setTitleColumn(e.target.value)}
              placeholder="Enter name list..."
            />
            <div className={cx("actions")}>
              <Button primary onClick={handleAddColumn}>
                Add Column
              </Button>
              <button
                className={cx("cancelBtn")}
                onClick={() => setIsAddColumn(false)}
              >
                <MdCancel />
              </button>
            </div>
          </div>
        )}
      </div>

      {/* Drag Overlay: Hình ảnh mờ đi theo chuột khi kéo */}
      <DragOverlay dropAnimation={null}>
        {!activeDragItem ? null : activeDragItem.type === "COLUMN" ? (
          <Column column={activeDragItem} />
        ) : (
          <TaskCard task={activeDragItem} isOverlay />
        )}
      </DragOverlay>
    </DndContext>
  );
};

export default BoardCanvas;
