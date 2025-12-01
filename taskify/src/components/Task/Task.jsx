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

import { authService, taskService } from "../../services/api.jsx";

import styles from "./Task.module.scss";
import TaskInput from "./TaskInput/TaskInput.jsx";
import TaskItem from "./TaskItem/TaskItem.jsx";
import ColumnTasks from "./ColumnTasks/ColumnTasks.jsx";
const cx = classNames.bind(styles);

const Task = () => {
  const [tasks, setTasks] = useState([]);
  const [activeId, setActiveId] = useState(null);
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 5 } })
  );
  // 1. Kiểm tra lúc mở app: Có token trong localStorage không?
  useEffect(() => {
    const fetchTasks = async () => {
      try {
        const data = await taskService.getAll();
        console.log("Dữ liệu task:", data);
        setTasks(data);
      } catch (error) {
        console.error("Lỗi tải task (có thể token hết hạn)", error);
      }
    };
    fetchTasks();
  }, []);

  // Handle add task
  const handleAddTask = async (task) => {
    const title = task.title || "";
    if (!title.trim()) alert("Tiêu đề không được để trống!");
    try {
      // Gọi API tạo task
      const newTask = await taskService.create(title);
      // Backend trả về task mới (có ID thật từ DB), thêm vào State
      setTasks([...tasks, newTask]);
    } catch (error) {
      alert("Không thể thêm task!");
    }
  };

  // Handle delete task
  const handleDeleteTask = async (id) => {
    if (window.confirm("Bạn chắc chắn muốn xóa?")) {
      try {
        await taskService.delete(id);
        // Xóa thành công trên Server thì mới xóa ở Frontend
        setTasks(tasks.filter((t) => t.id !== id));
      } catch (error) {
        alert("Lỗi khi xóa!");
      }
    }
  };

  // Handle edit task
  const handleUpdateTask = async (id, { fieldName, value }) => {
    try {
      // Tìm task cũ để lấy status hiện tại (vì API yêu cầu gửi cả cục object)
      const oldTask = tasks.find((t) => t.id === id);

      const updatedTask = await taskService.update(id, {
        ...oldTask,
        [fieldName]: value,
      });

      setTasks(tasks.map((t) => (t.id === id ? updatedTask : t)));
      // setEditingTask(null); // Đóng modal
    } catch (error) {
      alert("Lỗi cập nhật!");
    }
  };

  // Định nghĩa danh sách các cột
  const columns = [
    { id: "todo", title: "To Do" },
    { id: "doing", title: "Doing" },
    { id: "done", title: "Done" },
  ];

  // --- LOGIC XỬ LÝ KÉO THẢ (QUAN TRỌNG) ---
  // --- HÀM 1: BẮT ĐẦU KÉO (QUAN TRỌNG ĐỂ HIỆN OVERLAY) ---
  const handleDragStart = (event) => {
    setActiveId(event.active.id); // <--- Lưu ID lại ngay lập tức
  };
  // --- HÀM 2: KHI THẢ RA ---
  const handleDragEnd = async (event) => {
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
      // 1. Cập nhật UI ngay lập tức (Optimistic Update) để mượt mà
      const updatedTasks = tasks.map((t) =>
        t.id === activeId ? { ...t, status: newStatus } : t
      );
      setTasks(updatedTasks);

      // 2. Gọi API để lưu xuống DB (Chạy ngầm)
      try {
        await taskService.update(activeId, {
          ...activeTask,
          status: newStatus, // Gửi status mới lên
        });
      } catch (error) {
        // Nếu lỗi thì revert (hoàn tác) lại UI (Optional)
        console.error("Lỗi lưu vị trí kéo thả");
        fetchTasks(); // Load lại dữ liệu cũ
      }
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
