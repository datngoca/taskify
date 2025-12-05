import { createContext, useEffect, useReducer, useState } from "react";

import { taskReducer, initialState } from "./taskReducer";
import { ACTION_TYPES } from "./actionType";
import taskService from "@/services/taskService";
import { COLUMNS } from "../constants";

const TaskContext = createContext(null);

const TaskProvider = ({ children }) => {
  const [state, dispatch] = useReducer(taskReducer, initialState);
  const [activeId, setActiveId] = useState(null);

  useEffect(() => {
    const fetchTasks = async () => {
      dispatch({ type: ACTION_TYPES.FETCH_TASKS });
      try {
        const data = await taskService.getAll();
        dispatch({ type: ACTION_TYPES.FETCH_TASKS_SUCCESS, payload: data });
      } catch (error) {
        dispatch({
          type: ACTION_TYPES.FETCH_TASK_FAIL,
          payload: error.message,
        });
      }
    };
    fetchTasks();
  }, []);
  const handleCreateTask = async (task) => {
    const tempId = `temp-${Date.now()}`;
    // Tạo object task hoàn chỉnh để hiện lên UI
    const optimisticTask = {
      ...task,
      id: tempId,
    };
    dispatch({ type: ACTION_TYPES.CREATE_TASK, payload: optimisticTask });
    try {
      const newTask = await taskService.create(task);

      dispatch({
        type: ACTION_TYPES.CREATE_TASK_SUCCESS,
        payload: { tempId: tempId, task: newTask },
      });
    } catch (error) {
      // 5. THẤT BẠI -> XÓA TASK CÓ TEMP ID
      dispatch({
        type: ACTION_TYPES.CREATE_TASK_FAIL,
        payload: {
          tempId: tempId,
          error: error.message,
        },
      });
    }
  };

  const handleUpdateTask = async (task) => {
    const originalTask = state.tasks.find((t) => t.id === task.id);
    if (!originalTask) return;

    dispatch({ type: ACTION_TYPES.UPDATE_TASK });

    try {
      await taskService.update(task);
      dispatch({
        type: ACTION_TYPES.UPDATE_TASK_SUCCESS,
      });
    } catch (error) {
      dispatch({
        type: ACTION_TYPES.UPDATE_TASK_FAIL,
        payload: {
          error: error.message,
          task: originalTask,
        },
      });
    }
  };

  const handleDeleteTask = async (taskId) => {
    // --- BƯỚC 1: LƯU DỮ LIỆU CŨ ĐỂ BACKUP ---
    const taskIndex = state.tasks.findIndex((t) => t.id === taskId);
    const taskToDelete = state.tasks[taskIndex];

    if (!taskToDelete) return; // Không tìm thấy thì thôi

    // --- BƯỚC 2: OPTIMISTIC UPDATE (Xóa trên UI ngay) ---
    dispatch({
      type: ACTION_TYPES.DELETE_TASK,
      payload: { id: taskId },
    });
    try {
      await taskService.remove(taskId);
      dispatch({ type: ACTION_TYPES.DELETE_TASK_SUCCESS });
    } catch (error) {
      // --- BƯỚC 4: THẤT BẠI -> HOÀN TÁC (Gửi task và index cũ về) ---
      dispatch({
        type: ACTION_TYPES.DELETE_TASK_FAIL,
        payload: {
          error: error.message,
          task: taskToDelete, // Task cần khôi phục
          index: taskIndex, // Vị trí cần chèn lại
        },
      });
    }
  };
  const handleDragStart = (event) => {
    setActiveId(event.active.id);
  };

  const handleDragEnd = async (event) => {
    const { active, over } = event;
    setActiveId(null);

    if (!over) return;
    if (active.id === over.id) return;

    const originalTasks = [...state.tasks];
    const activeTask = originalTasks.find((t) => t.id === active.id);
    const overTask = originalTasks.find((t) => t.id === over.id);

    let newStatus = activeTask.status;
    if (COLUMNS.some((col) => col.id === over.id)) {
      newStatus = over.id;
    } else if (overTask) {
      newStatus = overTask.status;
    }

    const updatedTask = { ...activeTask, status: newStatus };

    // Optimistic update
    dispatch({
      type: ACTION_TYPES.DRAG_TASK_OPTIMISTIC,
      payload: updatedTask,
    });

    try {
      await taskService.update(updatedTask);
      dispatch({ type: ACTION_TYPES.DRAG_TASK_SUCCESS });
    } catch (error) {
      dispatch({
        type: ACTION_TYPES.DRAG_TASK_FAIL,
        payload: { error: error.message, tasks: originalTasks },
      });
    }
  };
  const activeTask = state.tasks.find((t) => t.id === activeId);

  const value = {
    tasks: state.tasks,
    activeId,
    activeTask,
    handleCreateTask,
    handleUpdateTask,
    handleDeleteTask,
    handleDragStart,
    handleDragEnd,
  };

  return <TaskContext.Provider value={value}>{children}</TaskContext.Provider>;
};
export { TaskContext, TaskProvider };
