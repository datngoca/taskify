import { useState, useEffect } from "react";
import classNames from "classnames/bind";

import styles from "./Task.module.scss";
import TaskInput from "./TaskInput/TaskInput.jsx";
import TaskList from "./TaskList/TaskList.jsx";
import TaskFilter from "./TaskFilter/TaskFilter.jsx";

const cx = classNames.bind(styles);

const STORAGE_KEY = "tasks";

function loadTasksFromStorage() {
  const storedTasks = localStorage.getItem(STORAGE_KEY);
  return storedTasks ? JSON.parse(storedTasks) : [];
}

const Task = () => {
  // khởi tạo từ localStorage
  const initial = loadTasksFromStorage();

  const [tasks, setTasks] = useState(initial);
  const [dataFilter, setDataFilter] = useState(tasks);

  // persist khi items thay đổi
  useEffect(() => {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(tasks));
    setDataFilter(tasks);
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

  const handleFilterTasks = (filter) => {
    switch (filter) {
      case "Completed":
        const completedTask = tasks.filter((task) => task.completed);
        setDataFilter(completedTask);
        break;
      case "Incomplete":
        const incompleteTasks = tasks.filter((task) => !task.completed);
        setDataFilter(incompleteTasks);
        break;
      case "All":
        setDataFilter(tasks);
        break;
      default:
        throw new Error("Invalid filter option");
    }
  };

  return (
    <>
      <div className={cx("wrapper")}>
        <TaskInput onAddTask={handleAddTask} />
        <TaskFilter onFilterTask={handleFilterTasks} />
        <TaskList
          tasks={dataFilter}
          onDeleteTask={handleDeleteTask}
          onSaveTask={handleUpdateTask}
          onCheckTask={handleUpdateTask}
        />
      </div>
    </>
  );
};
export default Task;
