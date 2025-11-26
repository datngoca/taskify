import { useState } from "react";
import TaskInput from "./TaskInput/TaskInput.jsx";
import TaskList from "./TaskList/TaskList.jsx";

const taskList = [
  {
    id: 1,
    name: "Task 1",
    completed: false,
  },
  {
    id: 2,
    name: "Task 2",
    completed: true,
  },
  {
    id: 3,
    name: "Task 3",
    completed: false,
  },
];
const Task = () => {
  const [tasks, setTasks] = useState(taskList);
  const handleDeleteTask = (idTask) => {
    const newTasks = tasks.filter((task) => task.id !== idTask);
    setTasks(newTasks);
  };
  const handleEditTask = (idTask, editedValue) => {
    const newTasks = tasks.map((task) => {
      if (task.id === idTask) {
        return { ...task, name: editedValue };
      }
      return task;
    });
    console.log(newTasks);
    setTasks(newTasks);
  };
  const handleAddTask = (task) => {
    const newTask = {
      id: tasks.length + 1,
      ...task,
    };
    setTasks([...tasks, newTask]);
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
