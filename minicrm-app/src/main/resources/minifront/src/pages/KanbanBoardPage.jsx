import React, { useEffect, useState } from "react";
import {
  Box,
  Button,
  Card,
  CardContent,
  Divider,
  Drawer,
  IconButton,
  InputAdornment,
  Paper,
  Stack,
  TextField,
  Typography,
  Tabs,
  Tab,
  List,
  ListItem,
  ListItemText,
} from "@mui/material";
import AddIcon from "@mui/icons-material/Add";
import MoreVertIcon from "@mui/icons-material/MoreVert";
import NavBar from "../components/NavBar";
import axios from "axios";

const KanbanBoardPage = () => {
  const [columns, setColumns] = useState([]);
  const [tasks, setTasks] = useState([]);
  const [users, setUsers] = useState([]);
  const [newColumnName, setNewColumnName] = useState("");
  const [newTaskTitle, setNewTaskTitle] = useState("");
  const [newTaskDescription, setNewTaskDescription] = useState("");
  const [selectedColumnId, setSelectedColumnId] = useState("");
  const [isAddingTask, setIsAddingTask] = useState(false);
  const [isAddingColumn, setIsAddingColumn] = useState(false);
  const [isTaskDrawerOpen, setIsTaskDrawerOpen] = useState(false);
  const [isColumnDrawerOpen, setIsColumnDrawerOpen] = useState(false);
  const [selectedTask, setSelectedTask] = useState(null);
  const [selectedColumn, setSelectedColumn] = useState(null);
  const [editedTaskTitle, setEditedTaskTitle] = useState("");
  const [editedTaskDescription, setEditedTaskDescription] = useState("");
  const [editedColumnName, setEditedColumnName] = useState("");
  const [activeTab, setActiveTab] = useState(0);
  const [notes, setNotes] = useState([]);
  const [newNote, setNewNote] = useState("");

  const fetchUsers = async () => {
    try {
      const { data } = await axios.get("/users");
      setUsers(data);
    } catch (error) {
      console.error("Error fetching users:", error);
    }
  };

  const fetchStatus = async () => {
    try {
      const { data } = await axios.get("/kanban/status");
      setColumns(data);
    } catch (error) {
      console.error("Error fetching columns:", error);
    }
  };

  const fetchTasks = async () => {
    try {
      const { data } = await axios.get("/kanban/task");
      setTasks(data);
    } catch (error) {
      console.error("Error fetching tasks:", error);
    }
  };

  const fetchNotes = async (taskId) => {
    try {
      const { data } = await axios.get(`/kanban/notes/${taskId}`);
      setNotes(data);
    } catch (error) {
      console.error("Error fetching notes:", error);
    }
  };

  const addNote = async () => {
    if (!newNote.trim() || !selectedTask) return;

    try {
      await axios.post("/kanban/notes", {
        message: newNote,
        task_id: selectedTask.id,
      });
      setNewNote("");
      fetchNotes(selectedTask.id);
    } catch (error) {
      console.error("Error adding note:", error);
    }
  };

  useEffect(() => {
    fetchStatus();
    fetchTasks();
    fetchUsers();
  }, []);

  const getUsernameById = (ownerId) => {
    const user = users.find((user) => user.id === ownerId);
    return user ? user.username : `ID: ${ownerId}`;
  };

  const onDragStart = (e, id) => {
    e.dataTransfer.setData("taskId", id);
  };

  const onDrop = async (e, status) => {
    const taskId = e.dataTransfer.getData("taskId");
    try {
      await axios.patch("/kanban/task/new-status", {
        task_id: Number(taskId),
        status_id: Number(status),
      });
      fetchStatus();
      fetchTasks();
    } catch (error) {
      console.error("Error update status task:", error);
    }
  };

  const addColumn = async () => {
    try {
      await axios.post("/kanban/status", { name: newColumnName });
      setNewColumnName("");
      setIsAddingColumn(false);
      fetchStatus();
    } catch (error) {
      console.error("Error adding column:", error);
    }
  };

  const updateColumn = async () => {
    try {
      await axios.patch(`/kanban/status/${selectedColumn.id}`, {
        name: editedColumnName,
      });
      fetchStatus();
      closeColumnDrawer();
    } catch (error) {
      console.error("Error updating column:", error);
    }
  };

  const deleteColumn = async () => {
    try {
      await axios.delete(`/kanban/status/${selectedColumn.id}`);
      fetchStatus();
      closeColumnDrawer();
    } catch (error) {
      console.error("Error deleting column:", error);
    }
  };

  const addTask = async () => {
    try {
      await axios.post("/kanban/task", {
        title: newTaskTitle,
        description: newTaskDescription,
        status_id: Number(selectedColumnId),
        owner: Number(
            users.find((u) => u.username === localStorage.getItem("username"))?.id
        ),
      });
      fetchTasks();
      setNewTaskTitle("");
      setNewTaskDescription("");
      setIsAddingTask(false);
    } catch (error) {
      console.error("Error adding task:", error);
    }
  };

  const openTaskDrawer = (task) => {
    setSelectedTask(task);
    setEditedTaskTitle(task.title);
    setEditedTaskDescription(task.description);
    setIsTaskDrawerOpen(true);
    setActiveTab(0);
    fetchNotes(task.id);
  };

  const openColumnDrawer = (column) => {
    setSelectedColumn(column);
    setEditedColumnName(column.name);
    setIsColumnDrawerOpen(true);
  };

  const closeTaskDrawer = () => {
    setIsTaskDrawerOpen(false);
    setSelectedTask(null);
    setNotes([]);
  };

  const closeColumnDrawer = () => {
    setIsColumnDrawerOpen(false);
    setSelectedColumn(null);
  };

  const updateTask = async () => {
    try {
      await axios.patch(`/kanban/task/${selectedTask.id}`, {
        title: editedTaskTitle,
        description: editedTaskDescription,
        profit: selectedTask.profit,
      });
      fetchTasks();
      closeTaskDrawer();
    } catch (error) {
      console.error("Error updating task:", error);
    }
  };

  const handleTabChange = (event, newValue) => {
    setActiveTab(newValue);
  };

  const formatDate = (dateString) => {
    const date = new Date(dateString);
    return date.toLocaleString();
  };

  return (
      <>
        <NavBar />
        <Stack
            direction="row"
            sx={{
              overflowX: "auto",
              width: "100%",
              height: "85vh",
              display: "flex",
            }}
        >
          {columns.map((column) => (
              <Paper
                  key={column.id}
                  onDragOver={(e) => e.preventDefault()}
                  onDrop={(e) => onDrop(e, column.id)}
                  sx={{
                    overflowY: "auto",
                    height: "95%",
                    display: "flex",
                    flexDirection: "column",
                    width: "380px",
                    flexShrink: 0,
                    marginY: 1,
                    marginX: 2,
                  }}
                  elevation={3}
              >
                <Box sx={{ display: "flex", alignItems: "center", margin: 2 }}>
                  <Typography component="h5" variant="h5" sx={{ flexGrow: 1 }}>
                    {column.name}
                  </Typography>
                  <IconButton onClick={() => openColumnDrawer(column)}>
                    <MoreVertIcon />
                  </IconButton>
                </Box>
                <Divider />
                <Box sx={{ overflowY: "auto", flexGrow: 1 }}>
                  {tasks
                      .filter((t) => t.status.id === column.id)
                      .map((task) => (
                          <Card
                              variant="outlined"
                              key={task.id}
                              draggable
                              onDragStart={(e) => onDragStart(e, task.id)}
                              sx={{ margin: 1, position: "relative" }}
                          >
                            <CardContent>
                              <Typography variant="h6" component="h6">
                                {task.title}
                              </Typography>
                              <Typography
                                  gutterBottom
                                  sx={{ color: "text.secondary", fontSize: 14 }}
                              >
                                {task.description}
                              </Typography>
                              {task.profit && (
                                  <Typography sx={{ fontWeight: "bold", color: "green" }}>
                                    {`${task.profit.toLocaleString("ru-RU")} ₽`}
                                  </Typography>
                              )}
                              {task.ownerId && (
                                  <Typography
                                      sx={{
                                        fontSize: 12,
                                        color: "text.secondary",
                                        mt: 1,
                                      }}
                                  >
                                    Владелец: {getUsernameById(task.ownerId)}
                                  </Typography>
                              )}
                            </CardContent>
                            <IconButton
                                sx={{ position: "absolute", top: 8, right: 8 }}
                                onClick={() => openTaskDrawer(task)}
                            >
                              <MoreVertIcon />
                            </IconButton>
                          </Card>
                      ))}
                </Box>
                <Box
                    sx={{
                      display: "flex",
                      flexDirection: "column",
                      alignItems: "center",
                      justifyContent: "center",
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
                        <Button
                            variant="contained"
                            onClick={() => addTask(column.id)}
                        >
                          Add Task
                        </Button>
                        <Button
                            variant="outlined"
                            onClick={() => setIsAddingTask(false)}
                        >
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
                          sx={{ width: "100%", height: "50px" }}
                      >
                        Add Task
                      </Button>
                  )}
                </Box>
              </Paper>
          ))}
          <Paper
              sx={{
                border: "2px dashed grey",
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                width: "380px",
                marginY: 1,
                height: "94%",
                flexShrink: 0,
                cursor: "pointer",
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
                      sx={{ marginBottom: 2, width: "100%" }}
                  />
                  <Button variant="contained" onClick={addColumn}>
                    Add Column
                  </Button>
                  <Button
                      variant="outlined"
                      onClick={(e) => {
                        e.stopPropagation();
                        setIsAddingColumn(false);
                      }}
                  >
                    Cancel
                  </Button>
                </>
            ) : (
                <>
                  <AddIcon sx={{ fontSize: 40, color: "grey" }} />
                  <Typography variant="body1" color="grey">
                    Add Column
                  </Typography>
                </>
            )}
          </Paper>
        </Stack>
        <Drawer anchor="right" open={isTaskDrawerOpen} onClose={closeTaskDrawer}>
          <Box sx={{ width: 380, padding: 2, height: "100%", display: "flex", flexDirection: "column" }}>
            <Tabs value={activeTab} onChange={handleTabChange}>
              <Tab label="Details" />
              <Tab label="Notes" />
            </Tabs>

            {activeTab === 0 && (
                <Box sx={{ flexGrow: 1 }}>
                  <Typography variant="h6" component="h6" sx={{ mt: 2 }}>
                    Edit Task
                  </Typography>
                  <TextField
                      label="Task Title"
                      value={editedTaskTitle}
                      onChange={(e) => setEditedTaskTitle(e.target.value)}
                      sx={{ marginBottom: 2, mt: 2 }}
                      fullWidth
                  />
                  <TextField
                      label="Task Description"
                      value={editedTaskDescription}
                      onChange={(e) => setEditedTaskDescription(e.target.value)}
                      sx={{ marginBottom: 2 }}
                      fullWidth
                      multiline
                      rows={4}
                  />
                  <TextField
                      label="Profit (₽)"
                      type="number"
                      value={selectedTask?.profit || ""}
                      onChange={(e) =>
                          setSelectedTask({
                            ...selectedTask,
                            profit: parseFloat(e.target.value) || 0,
                          })
                      }
                      sx={{ marginBottom: 2 }}
                      fullWidth
                      InputProps={{
                        endAdornment: <InputAdornment position="end">₽</InputAdornment>,
                      }}
                  />
                  <Box sx={{ display: "flex", justifyContent: "flex-end", mt: 2 }}>
                    <Button variant="outlined" onClick={closeTaskDrawer} sx={{ mr: 2 }}>
                      Cancel
                    </Button>
                    <Button variant="contained" onClick={updateTask}>
                      Save
                    </Button>
                  </Box>
                </Box>
            )}

            {activeTab === 1 && (
                <Box sx={{ flexGrow: 1, display: "flex", flexDirection: "column" }}>
                  <Typography variant="h6" component="h6" sx={{ mt: 2 }}>
                    Task Notes
                  </Typography>
                  <List sx={{ flexGrow: 1, overflowY: "auto", mb: 2 }}>
                    {notes.map((note) => (
                        <ListItem key={note.id} sx={{ flexDirection: "column", alignItems: "flex-start" }}>
                          <Typography variant="body2" color="text.secondary">
                            {formatDate(note.date)}
                          </Typography>
                          <ListItemText primary={note.message} />
                          <Divider sx={{ width: "100%", my: 1 }} />
                        </ListItem>
                    ))}
                  </List>
                  <Box sx={{ display: "flex", mt: "auto" }}>
                    <TextField
                        label="New Note"
                        value={newNote}
                        onChange={(e) => setNewNote(e.target.value)}
                        fullWidth
                        multiline
                        rows={2}
                        sx={{ mr: 1 }}
                    />
                    <Button
                        variant="contained"
                        onClick={addNote}
                        disabled={!newNote.trim()}
                        sx={{ height: "56px" }}
                    >
                      Send
                    </Button>
                  </Box>
                </Box>
            )}
          </Box>
        </Drawer>
        <Drawer
            anchor="right"
            open={isColumnDrawerOpen}
            onClose={closeColumnDrawer}
        >
          <Box sx={{ width: 380, padding: 2 }}>
            <Typography variant="h6" component="h6">
              Edit Column
            </Typography>
            <TextField
                label="Column Name"
                value={editedColumnName}
                onChange={(e) => setEditedColumnName(e.target.value)}
                sx={{ marginBottom: 2 }}
                fullWidth
            />
            <Box sx={{ display: "flex", justifyContent: "space-between", mt: 3 }}>
              <Button
                  variant="contained"
                  color="error"
                  onClick={deleteColumn}
                  sx={{ mr: 2 }}
              >
                Delete Column
              </Button>
              <Box>
                <Button
                    variant="outlined"
                    onClick={closeColumnDrawer}
                    sx={{ mr: 2 }}
                >
                  Cancel
                </Button>
                <Button variant="contained" onClick={updateColumn}>
                  Save
                </Button>
              </Box>
            </Box>
          </Box>
        </Drawer>
      </>
  );
};

export default KanbanBoardPage;