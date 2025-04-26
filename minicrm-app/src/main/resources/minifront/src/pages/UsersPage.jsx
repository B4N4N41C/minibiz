import React, { useEffect, useState } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
  IconButton,
  Button,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogActions,
  TextField,
  Box,
  Typography,
  CircularProgress,
  Snackbar,
  Alert,
} from "@mui/material";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import AddIcon from "@mui/icons-material/Add";
import axios from "axios";
import NavBar from '../components/NavBar.jsx'

const UsersPage = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [openDialog, setOpenDialog] = useState(false);
  const [currentUser, setCurrentUser] = useState(null);
  const [openDeleteDialog, setOpenDeleteDialog] = useState(false);
  const [userToDelete, setUserToDelete] = useState(null);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: "",
    severity: "success",
  });

  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: ""
  });

  useEffect(() => {
    fetchUsers();
  }, []);

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const { data } = await axios.get("/users");
      setUsers(data);
    } catch (error) {
      console.error("Error fetching users:", error);
      setSnackbar({
        open: true,
        message: "Failed to fetch users",
        severity: "error",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleEditClick = (user) => {
    setCurrentUser(user);
    setFormData({
      username: user.username,
      email: user.email,
    });
    setOpenDialog(true);
  };

  const handleAddClick = () => {
    setCurrentUser(null);
    setFormData({
      username: "",
      email: "",
    });
    setOpenDialog(true);
  };

  const handleDeleteClick = (user) => {
    setUserToDelete(user);
    setOpenDeleteDialog(true);
  };

  const handleSubmit = async () => {
    try {
      if (currentUser) {
        await axios.patch(`/users/${currentUser.id}`, {
          username: formData.username,
          email: formData.email,
          password: formData.password
        });
        setSnackbar({
          open: true,
          message: "Пользователь обновлен",
          severity: "success",
        });
      } else {
        // Отправка на эндпоинт регистрации
        await axios.post("/auth/sign-up", {
          username: formData.username,
          email: formData.email,
          password: formData.password
        });
        setSnackbar({
          open: true,
          message: "Пользователь зарегистрирован",
          severity: "success",
        });
      }
      fetchUsers();
      setOpenDialog(false);
    } catch (error) {
      console.error("Ошибка:", error);
      setSnackbar({
        open: true,
        message: error.response?.data?.message || "Ошибка операции",
        severity: "error",
      });
    }
  };

  const handleDelete = async () => {
    try {
      await axios.delete(`/users/${userToDelete.id}`);
      setSnackbar({
        open: true,
        message: "User deleted successfully",
        severity: "success",
      });
      fetchUsers();
    } catch (error) {
      console.error("Error deleting user:", error);
      setSnackbar({
        open: true,
        message: "Error deleting user",
        severity: "error",
      });
    } finally {
      setOpenDeleteDialog(false);
    }
  };

  const handleCloseSnackbar = () => {
    setSnackbar({ ...snackbar, open: false });
  };

  return (
    <>
    <NavBar />
    <Box sx={{ padding: 3 }}>
      <Typography variant="h4" gutterBottom>
        Список пользователей
      </Typography>

      <Button
        variant="contained"
        startIcon={<AddIcon />}
        onClick={handleAddClick}
        sx={{ mb: 2 }}
      >
        Добавить пользователя
      </Button>

      {loading ? (
        <Box sx={{ display: "flex", justifyContent: "center", mt: 4 }}>
          <CircularProgress />
        </Box>
      ) : (
        <TableContainer component={Paper}>
          <Table>
            <TableHead>
              <TableRow>
                <TableCell>ID</TableCell>
                <TableCell>Имя</TableCell>
                <TableCell>Почта</TableCell>
                <TableCell align="right">Actions</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {users.map((user) => (
                <TableRow key={user.id}>
                  <TableCell>{user.id}</TableCell>
                  <TableCell>{user.username}</TableCell>
                  <TableCell>{user.email}</TableCell>
                  <TableCell align="right">
                    <IconButton
                      aria-label="edit"
                      onClick={() => handleEditClick(user)}
                    >
                      <EditIcon />
                    </IconButton>
                    <IconButton
                      aria-label="delete"
                      onClick={() => handleDeleteClick(user)}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      )}
      <Dialog open={openDialog} onClose={() => setOpenDialog(false)}>
        <DialogTitle>
          {currentUser ? "Edit User" : "Add New User"}
        </DialogTitle>
        <DialogContent>
          <Box sx={{ mt: 2 }}>
            <TextField
              fullWidth
              label="Имя"
              name="username"
              value={formData.username}
              onChange={handleInputChange}
              margin="normal"
            />
            <TextField
              fullWidth
              label="Почта"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
              margin="normal"
            />
            <TextField
              fullWidth
              label="Пароль"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleInputChange}
              margin="normal"
            />
          </Box>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDialog(false)}>Cancel</Button>
          <Button onClick={handleSubmit} variant="contained">
            {currentUser ? "Update" : "Create"}
          </Button>
        </DialogActions>
      </Dialog>
      <Dialog
        open={openDeleteDialog}
        onClose={() => setOpenDeleteDialog(false)}
      >
        <DialogTitle>Confirm Delete</DialogTitle>
        <DialogContent>
          <Typography>
            Are you sure you want to delete user "{userToDelete?.username}"?
          </Typography>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenDeleteDialog(false)}>Cancel</Button>
          <Button onClick={handleDelete} color="error" variant="contained">
            Delete
          </Button>
        </DialogActions>
      </Dialog>
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
      >
        <Alert
          onClose={handleCloseSnackbar}
          severity={snackbar.severity}
          sx={{ width: "100%" }}
        >
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Box>
    </>
  );
};

export default UsersPage;