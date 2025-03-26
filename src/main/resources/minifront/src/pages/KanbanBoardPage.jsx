import React, { useEffect, useState } from 'react'
import { Box, Button, Card, CardContent, Divider, Paper, Stack, TextField, Typography } from '@mui/material'
import AddIcon from '@mui/icons-material/Add';

const KanbanBoardPage = () => {
  const [columns, setColumns] = useState([])
  const [tasks, setTasks] = useState([])
  const [newColumnName, setNewColumnName] = useState('')
  const [newTaskTitle, setNewTaskTitle] = useState('')
  const [newTaskDescription, setNewTaskDescription] = useState('')
  const [selectedColumnId, setSelectedColumnId] = useState('')
  const [isAddingTask, setIsAddingTask] = useState(false)
  const [isAddingColumn, setIsAddingColumn] = useState(false);

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
        setIsAddingColumn(false);
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
        fetchStatus()
        fetchTasks()
        setNewTaskTitle('')
        setNewTaskDescription('')
        setIsAddingTask(false)
      })
      .catch((error) => console.error('Error adding task:', error))
  }

  return (
    <Stack direction="row" spacing={3} sx={{
      overflowX: 'auto',
      width: '100%',
      height: '95vh',
      display: 'flex'
    }}>
      {columns.map((column) => (
          <Paper
            key={column.id}
            onDragOver={(e) => e.preventDefault()}
            onDrop={(e) => onDrop(e, column.id)}
            sx={{
              overflowY: 'auto',
              height: '99%',
              display: 'flex',
              flexDirection: 'column',
              width: '380px',
              flexShrink: 0
            }}
          >
            <Typography component="h5" variant="h5" sx={{ margin: 2 }}>{column.name}</Typography>
            <Divider />
            <Box
              sx={{
                overflowY: 'auto',
                flexGrow: 1
              }}
            >
              {tasks.filter((t) => t.status.id === column.id).map((task) =>
                (
                  <Card
                    variant="outlined"
                    key={task.id}
                    draggable
                    onDragStart={(e) => onDragStart(e, task.id)}
                    sx={{ margin: 1 }}
                  >
                    <CardContent>
                      <Typography variant="h6" component="h6">{task.title}</Typography>
                      <Typography gutterBottom
                                  sx={{ color: 'text.secondary', fontSize: 14 }}>{task.description}</Typography>
                    </CardContent>
                  </Card>
                ))}
            </Box>
            <Box
              sx={{
                display: 'flex',
                flexDirection: 'column',
                alignItems: 'center',
                justifyContent: 'center',
                padding: 2,
              }}
            >
              {isAddingTask && selectedColumnId === column.id ? (
                <>
                  <TextField
                    label="Task Title"
                    value={newTaskTitle}
                    onChange={(e) => setNewTaskTitle(e.target.value)}
                    sx={{ marginBottom: 2 }}
                  />
                  <TextField
                    label="Task Description"
                    value={newTaskDescription}
                    onChange={(e) => setNewTaskDescription(e.target.value)}
                    sx={{ marginBottom: 2 }}
                  />
                  <Button variant="contained" onClick={() => addTask(column.id)}>
                    Add Task
                  </Button>
                  <Button variant="outlined" onClick={() => setIsAddingTask(false)}>
                    Cancel
                  </Button>
                </>
              ) : (
                <Button
                  variant="contained"
                  onClick={() => {
                    setIsAddingTask(true);
                    setSelectedColumnId(column.id);
                  }}
                  sx={{ width: '100%', height: '50px' }}
                >
                  Add Task
                </Button>
              )}
            </Box>
          </Paper>
        ))}
      <Paper
        sx={{
          border: '2px dashed grey',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          width: '380px',
          height: '98%',
          flexShrink: 0,
          cursor: 'pointer',
        }}
        onClick={(e) => {
          if (!isAddingColumn) {
            setIsAddingColumn(true);
          }
          e.stopPropagation();
        }}
      >
        {isAddingColumn ? (
          <>
            <TextField
              label="Column Name"
              value={newColumnName}
              onChange={(e) => setNewColumnName(e.target.value)}
              sx={{ marginBottom: 2, width: '100%' }}
            />
            <Button variant="contained" onClick={addColumn}>
              Add Column
            </Button>
            <Button variant="outlined" onClick={(e) => {
              e.stopPropagation();
              setIsAddingColumn(false);
            }}>
              Cancel
            </Button>
          </>
        ) : (
          <>
            <AddIcon sx={{ fontSize: 40, color: 'grey' }} />
            <Typography variant="body1" color="grey">
              Add Column
            </Typography>
          </>
        )}
      </Paper>
    </Stack>
  )
}

export default KanbanBoardPage
