import React, { useEffect, useState } from "react";
import { Box, Typography, Paper, Grid2, CircularProgress } from "@mui/material";
import {
  BarChart,
  PieChart,
  Bar,
  Pie,
  Cell,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import axios from "axios";
import NavBar from "../components/NavBar";
import { useStore } from "../stores/mainStore";
import { useNotification } from '../services/useNotification.jsx';

const DashboardPage = () => {
  const [statistics, setStatistics] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const { moduleth } = useStore();
  const { showNotification, Notification } = useNotification();

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get("/api/statistics/dashboard");
        setStatistics(response.data);
        setLoading(false);
        showNotification('Статистика загружена', 'success');
      } catch (err) {
        setError(err.message);
        setLoading(false);
        const errorMessage = error.response?.data?.message || 'Ошибка при загрузки статистики';
        showNotification(errorMessage);
      }
    };

    fetchData();
  }, []);

  const COLORS = ["#0088FE", "#00C49F", "#FFBB28", "#FF8042", "#8884D8"];

  if (loading) {
    return (
        <Box display="flex" justifyContent="center" alignItems="center" minHeight="80vh">
          <CircularProgress />
        </Box>
    );
  }

  if (error) {
    return (
        <Box p={3}>
          <Typography color="error">Error loading data: {error}</Typography>
        </Box>
    );
  }

  return (
      <>
        <NavBar />
        <Box p={3}>
          <Typography variant="h4" gutterBottom>
            Статистика
          </Typography>
          <Grid2 container spacing={{ xs: 2, md: 3 }} direction="column">
            {moduleth?.includes("Logical name: chat") && (
                <Grid2 xs={12}>
                  <Paper elevation={3} sx={{ p: 2, height: "100%", display: "flex", flexDirection: "column" }}>
                    <Typography variant="h6" gutterBottom>
                      Количество сообщений для каждого пользователя.
                    </Typography>
                    <Box sx={{ flexGrow: 1 }}>
                      <ResponsiveContainer width="100%" height={400}>
                        <BarChart data={statistics.userStatistics}>
                          <CartesianGrid strokeDasharray="3 3" />
                          <XAxis dataKey="username" />
                          <YAxis />
                          <Tooltip />
                          <Legend />
                          <Bar dataKey="messages" fill="#8884d8" name="Сообщения" />
                        </BarChart>
                      </ResponsiveContainer>
                    </Box>
                  </Paper>
                </Grid2>
            )}
            {moduleth?.includes("Logical name: kanban") && (
                <Grid2 xs={12}>
                  <Paper elevation={3} sx={{ p: 2, height: "100%", display: "flex", flexDirection: "column" }}>
                    <Typography variant="h6" gutterBottom>
                      Количество сделок, созданных каждым пользователем.
                    </Typography>
                    <Box sx={{ flexGrow: 1 }}>
                      <ResponsiveContainer width="100%" height={400}>
                        <PieChart>
                          <Pie
                              data={statistics.userStatistics}
                              cx="50%"
                              cy="50%"
                              labelLine={false}
                              innerRadius={60}
                              outerRadius={150}
                              paddingAngle={1}
                              cornerRadius={5}
                              fill="#8884d8"
                              dataKey="tasks"
                              nameKey="username"
                              label={({ username, percent }) =>
                                  `${username}: ${(percent * 100).toFixed(0)}%`
                              }
                          >
                            {statistics.userStatistics.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                            ))}
                          </Pie>
                          <Tooltip formatter={(value) => [`${value} tasks`, "Count"]} />
                          <Legend />
                        </PieChart>
                      </ResponsiveContainer>
                    </Box>
                  </Paper>
                </Grid2>
            )}
            {moduleth?.includes("Logical name: kanban") && (
                <Grid2 xs={12}>
                  <Paper elevation={3} sx={{ p: 2, height: "100%", display: "flex", flexDirection: "column" }}>
                    <Typography variant="h6" gutterBottom>
                      Прибыль, которую принёс каждый сотрудник.
                    </Typography>
                    <Box sx={{ flexGrow: 1 }}>
                      <ResponsiveContainer width="100%" height={400}>
                        <BarChart data={statistics.userStatistics}>
                          <CartesianGrid strokeDasharray="3 3" />
                          <XAxis dataKey="username" />
                          <YAxis />
                          <Tooltip formatter={(value) => [`${value} ₽`, "Revenue"]} />
                          <Legend />
                          <Bar dataKey="profit" fill="#82ca9d" name="Прибыль (₽)" />
                        </BarChart>
                      </ResponsiveContainer>
                    </Box>
                  </Paper>
                </Grid2>
            )}
          </Grid2>
        </Box>
        <Notification />
      </>
  );
};

export default DashboardPage;