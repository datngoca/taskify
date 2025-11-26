import { useState } from "react";
import TaskInput from "./TaskInput/TaskInput.jsx";
import TaskList from "./TaskList/TaskList.jsx";

const Task = () => {
  if (localStorage.getItem("tasks") === null) {
    localStorage.setItem("tasks", JSON.stringify([]));
  }
  const taskList = localStorage.getItem("tasks")
    ? JSON.parse(localStorage.getItem("tasks"))
    : [];
  const [tasks, setTasks] = useState(taskList);

  const updateLocalStorage = (updatedTasks) => {
    localStorage.setItem("tasks", JSON.stringify(updatedTasks));
  };
  const handleDeleteTask = (idTask) => {
    const newTasks = tasks.filter((task) => task.id !== idTask);
    setTasks(newTasks);
    updateLocalStorage(newTasks);
  };
  const handleEditTask = (idTask, editedValue) => {
    const newTasks = tasks.map((task) => {
      if (task.id === idTask) {
        return { ...task, name: editedValue };
      }
      return task;
    });
    setTasks(newTasks);
    updateLocalStorage(newTasks);
  };
  const handleAddTask = (task) => {
    const newTask = {
      id: tasks.length + 1,
      ...task,
    };
    setTasks([...tasks, newTask]);
    updateLocalStorage([...tasks, newTask]);
  };
  return (
    <>
      <TaskInput onAddTask={handleAddTask} />
      <TaskList
        tasks={tasks}
        onDeleteTask={handleDeleteTask}
        onSaveTask={handleEditTask}
      />
    </>
  );
};
export default Task;
