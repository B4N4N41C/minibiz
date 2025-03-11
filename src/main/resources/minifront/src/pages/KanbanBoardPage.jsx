import React, { useState } from 'react';
import '../style/Kanban.css'

const KanbanBoardPage = () => {
  const [tasks, setTasks] = useState([
    { id: 1, text: 'Task 1', status: 'todo' },
    { id: 2, text: 'Task 2', status: 'in-progress' },
    { id: 3, text: 'Task 3', status: 'done' },
  ]);

  const statuses = ['todo', 'in-progress', 'done'];

  const onDragStart = (e, id) => {
    e.dataTransfer.setData('taskId', id);
  };

  const onDrop = (e, status) => {
    const taskId = e.dataTransfer.getData('taskId');
    const updatedTasks = tasks.map(task =>
      task.id === Number(taskId) ? { ...task, status } : task
    );
    setTasks(updatedTasks);
  };

  return (
    <div className="kanban-board">
      {statuses.map(status => (
        <div
          key={status}
          className="kanban-column"
          onDragOver={e => e.preventDefault()}
          onDrop={e => onDrop(e, status)}
        >
          <h2>{status.toUpperCase()}</h2>
          {tasks
            .filter(task => task.status === status)
            .map(task => (
              <div
                key={task.id}
                className="kanban-task"
                draggable
                onDragStart={e => onDragStart(e, task.id)}
              >
                {task.text}
              </div>
            ))}
        </div>
      ))}
    </div>
  );
};

export default KanbanBoardPage;