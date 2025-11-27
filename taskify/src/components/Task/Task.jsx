import { useState, useEffect } from "react";
import classNames from "classnames/bind";
import {
  DndContext,
  DragOverlay,
  closestCorners,
  PointerSensor,
  useSensor,
  useSensors,
} from "@dnd-kit/core";

import styles from "./Task.module.scss";
import TaskInput from "./TaskInput/TaskInput.jsx";
import TaskItem from "./TaskItem/TaskItem.jsx";
import ColumnTasks from "./ColumnTasks/ColumnTasks.jsx";

const cx = classNames.bind(styles);

const STORAGE_KEY = "tasks";

function loadTasksFromStorage() {
  const storedTasks = localStorage.getItem(STORAGE_KEY);
  return storedTasks ? JSON.parse(storedTasks) : [];
}
// Dữ liệu mới cho Level 2
const initialTasks = [
  { id: 1, title: "Học React", status: "todo" },
  { id: 2, title: "Làm bài tập", status: "doing" },
  { id: 3, title: "Ngủ", status: "done" },
];

const Task = () => {
  // khởi tạo từ localStorage
  const initial = loadTasksFromStorage();

  const [tasks, setTasks] = useState(initialTasks);
  const [activeId, setActiveId] = useState(null);
  // persist khi items thay đổi
  useEffect(() => {
    // localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
  }, [tasks]);

  // Handle delete task
  const handleDeleteTask = (idTask) => {
    const newTasks = tasks.filter((task) => task.id !== idTask);
    setTasks(newTasks);
  };

  // Handle edit task
  const handleUpdateTask = (idTask, { fieldName, value }) => {
    const newTasks = tasks.map((task) => {
      if (task.id === idTask) {
        return { ...task, [fieldName]: value };
      }
      return task;
    });
    setTasks(newTasks);
  };

  // Handle add task
  const handleAddTask = (task) => {
    setTasks((prev) => {
      const newTask = {
        id: tasks?.length ? tasks[tasks.length - 1].id + 1 : 1,
        ...task,
      };
      console.log(newTask);
      return [...prev, newTask];
    });
  };

  // Định nghĩa danh sách các cột
  const columns = [
    { id: "todo", title: "To Do" },
    { id: "doing", title: "Doing" },
    { id: "done", title: "Done" },
  ];

  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 5 } })
  );

  // --- LOGIC XỬ LÝ KÉO THẢ (QUAN TRỌNG) ---
  // --- HÀM 1: BẮT ĐẦU KÉO (QUAN TRỌNG ĐỂ HIỆN OVERLAY) ---
  const handleDragStart = (event) => {
    setActiveId(event.active.id); // <--- Lưu ID lại ngay lập tức
  };
  // --- HÀM 2: KHI THẢ RA ---
  const handleDragEnd = (event) => {
    setActiveId(null); // Reset trạng thái
    const { active, over } = event;
    if (!over) return;

    const activeId = active.id;
    const overId = over.id;

    // Tìm task đang được kéo
    const activeTask = tasks.find((t) => t.id === activeId);

    // Logic xác định trạng thái mới
    let newStatus = activeTask.status;

    // TRƯỜNG HỢP 1: Thả vào Cột (Todo/Doing/Done)
    if (columns.some((col) => col.id === overId)) {
      newStatus = overId;
    }
    // TRƯỜNG HỢP 2: Thả đè lên Task khác
    else {
      const overTask = tasks.find((t) => t.id === overId);
      if (overTask) {
        newStatus = overTask.status;
      }
    }

    // Cập nhật State
    if (activeTask.status !== newStatus) {
      setTasks((prev) =>
        prev.map((t) => (t.id === activeId ? { ...t, status: newStatus } : t))
      );
    }
  };

  const activeTask = tasks.find((t) => t.id === activeId);

  return (
    <>
      <div className={cx("container")}>
        <TaskInput onAddTask={handleAddTask} />
        {/* <TaskFilter onFilterTask={handleFilterTasks} /> */}
        <DndContext
          sensors={sensors}
          onDragStart={handleDragStart} // <--- 2. CHƯA CÓ DÒNG NÀY LÀ KHÔNG CHẠY
          collisionDetection={closestCorners}
          onDragEnd={handleDragEnd}
        >
          <div className={cx("board")}>
            {columns.map((col) => (
              <ColumnTasks
                key={col.id}
                col={col}
                tasks={tasks.filter((t) => t.status === col.id)}
                handleDeleteTask={handleDeleteTask}
                handleUpdateTask={handleUpdateTask}
              />
            ))}
          </div>
          {/* DRAG OVERLAY: Đây là phần tử "bay" theo chuột 
          Nó nằm đè lên trên tất cả (nhờ Portal của dnd-kit)
        */}
          <DragOverlay>
            {activeId && activeTask ? (
              <TaskItem task={activeTask} isOverlay={true} />
            ) : null}
          </DragOverlay>
        </DndContext>
      </div>
    </>
  );
};
export default Task;
