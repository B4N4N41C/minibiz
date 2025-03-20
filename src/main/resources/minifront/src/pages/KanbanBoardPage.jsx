import React, { useEffect, useState } from 'react'
import '../style/Kanban.css'

const KanbanBoardPage = () => {
  const [columns, setColumns] = useState([])
  const [tasks, setTasks] = useState([])
  const [newColumnName, setNewColumnName] = useState('')
  const [newTaskTitle, setNewTaskTitle] = useState('')
  const [newTaskDescription, setNewTaskDescription] = useState('')
  const [selectedColumnId, setSelectedColumnId] = useState('')

  const fetchStatus = (() => {
    fetch('/kanban/status')
      .then((response) => response.json())
      .then((data) => setColumns(data))
      .catch((error) => console.error('Error fetching columns:', error))
  })
  const fetchTasks = (() => {
    fetch('/kanban/task')
      .then((response) => response.json())
      .then((data) => setTasks(data))
      .catch((error) => console.error('Error fetching tasks:', error))
  })
  useEffect(() => {
    fetchStatus()
    fetchTasks()
  }, [])

  const onDragStart = (e, id) => {
    e.dataTransfer.setData('taskId', id)
  }

  const onDrop = (e, status) => {
    const taskId = e.dataTransfer.getData('taskId')

    fetch('/kanban/task/new-status', {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        task_id: Number(taskId),
        status_id: Number(status)
      })
    })
      .then((response) => response.json())
      .then((response) => {
        fetchStatus()
        fetchTasks()
      })
      .catch((error) => console.error('Error update status task:', error))
  }

  const addColumn = () => {
    fetch('/kanban/status', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ name: newColumnName })
    })
      .then((response) => response.json())
      .then((newColumn) => {
        setColumns([...columns, newColumn])
        setNewColumnName('')
      })
      .catch((error) => console.error('Error adding column:', error))
  }

  const addTask = () => {
    fetch('/kanban/task', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        title: newTaskTitle,
        description: newTaskDescription,
        status_id: Number(selectedColumnId)
      })
    })
      .then((response) => response.json())
      .then((newTask) => {
        const updatedColumns = columns.map((column) =>
          column.id === newTask.status_id
            ? { ...column, tasks: [...column.tasks, newTask] }
            : column
        )
        setColumns(updatedColumns)
        setNewTaskTitle('')
        setNewTaskDescription('')
      })
      .catch((error) => console.error('Error adding task:', error))
  }

  return (
    <div className="kanban-board">
      <div className="kanban-controls">
        <input
          type="text"
          value={newColumnName}
          onChange={(e) => setNewColumnName(e.target.value)}
          placeholder="New Column Name"
        />
        <button onClick={addColumn}>Add Column</button>
        <br />

        <select
          value={selectedColumnId}
          onChange={(e) => setSelectedColumnId(e.target.value)}
        >
          <option value="">Select Column</option>
          {columns.map((column) => (
            <option key={column.id} value={column.id}>
              {column.name}
            </option>
          ))}
        </select>
        <input
          type="text"
          value={newTaskTitle}
          onChange={(e) => setNewTaskTitle(e.target.value)}
          placeholder="New Task Title"
        />
        <input
          type="text"
          value={newTaskDescription}
          onChange={(e) => setNewTaskDescription(e.target.value)}
          placeholder="New Task Description"
        />
        <button onClick={addTask}>Add Task</button>
      </div>
      {columns.map((column) => (
          <div
            key={column.id}
            className="kanban-column"
            onDragOver={(e) => e.preventDefault()}
            onDrop={(e) => onDrop(e, column.id)}
          >
            <h2>{column.name}</h2>
            {tasks.filter((t) => t.status.id === column.id).map((task) =>
              (
                <div
                  key={task.id}
                  className="kanban-task"
                  draggable
                  onDragStart={(e) => onDragStart(e, task.id)}
                >
                  <h3>{task.title}</h3>
                  <p>{task.description}</p>
                </div>
              )
            )}
          </div>
        )
      )}
    </div>
  )
}

export default KanbanBoardPage
