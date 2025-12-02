// src/components/Task/hooks/useTaskBoard.js
import { useState, useEffect } from "react";
import { useSensor, useSensors, PointerSensor } from "@dnd-kit/core";
import taskService from "../services/taskService";
import { COLUMNS } from "../components/Task/constants";
export const useTaskBoard = () => {
  const [tasks, setTasks] = useState([]);
  const [activeId, setActiveId] = useState(null);

  // Cấu hình cảm biến kéo thả
  const sensors = useSensors(
    useSensor(PointerSensor, { activationConstraint: { distance: 5 } })
  );

  // 1. Fetch data
  useEffect(() => {
    fetchTasks();
  }, []);

  const fetchTasks = async () => {
    try {
      const data = await taskService.getAll();
      setTasks(data);
    } catch (error) {
      console.error("Lỗi tải task:", error);
    }
  };

  // 2. CRUD Handlers
  const handleAddTask = async (task) => {
    const title = task.title?.trim();
    if (!title) return alert("Tiêu đề không được để trống!");

    try {
      const newTask = await taskService.create(title);
      setTasks((prev) => [...prev, newTask]);
    } catch (error) {
      alert("Không thể thêm task!");
    }
  };

  const handleDeleteTask = async (id) => {
    if (!window.confirm("Bạn chắc chắn muốn xóa?")) return;
    try {
      await taskService.delete(id);
      setTasks((prev) => prev.filter((t) => t.id !== id));
    } catch (error) {
      alert("Lỗi khi xóa!");
      console.log("loi", error);
    }
  };

  const handleUpdateTask = async (id, { fieldName, value }) => {
    try {
      const oldTask = tasks.find((t) => t.id === id);
      const updatedTask = await taskService.update(id, {
        ...oldTask,
        [fieldName]: value,
      });
      setTasks((prev) => prev.map((t) => (t.id === id ? updatedTask : t)));
    } catch (error) {
      alert("Lỗi cập nhật!");
    }
  };

  // 3. Drag & Drop Handlers
  const handleDragStart = (event) => {
    setActiveId(event.active.id);
  };

  const handleDragEnd = async (event) => {
    const { active, over } = event;
    setActiveId(null);
    if (!over) return;

    const activeId = active.id;
    const overId = over.id;
    const activeTask = tasks.find((t) => t.id === activeId);

    // Tính toán trạng thái mới
    let newStatus = activeTask.status;

    // Nếu thả vào cột
    if (COLUMNS.some((col) => col.id === overId)) {
      newStatus = overId;
    } else {
      // Nếu thả đè lên task khác
      const overTask = tasks.find((t) => t.id === overId);
      if (overTask) newStatus = overTask.status;
    }

    if (activeTask.status !== newStatus) {
      // Optimistic Update (Cập nhật UI trước)
      const updatedTasks = tasks.map((t) =>
        t.id === activeId ? { ...t, status: newStatus } : t
      );
      setTasks(updatedTasks);

      // Gọi API ngầm
      try {
        await taskService.update(activeId, {
          ...activeTask,
          status: newStatus,
        });
      } catch (error) {
        console.error("Lỗi lưu vị trí, hoàn tác...");
        fetchTasks(); // Revert nếu lỗi
      }
    }
  };

  const activeTask = tasks.find((t) => t.id === activeId);

  // Trả về mọi thứ component cần
  return {
    tasks,
    activeId,
    activeTask,
    sensors,
    handleAddTask,
    handleDeleteTask,
    handleUpdateTask,
    handleDragStart,
    handleDragEnd,
  };
};
