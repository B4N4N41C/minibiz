import React, { useEffect, useState } from 'react';
import {
  Box,
  Typography,
  Paper,
  Grid2,
  CircularProgress
} from '@mui/material';
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
  ResponsiveContainer
} from 'recharts';
import axios from 'axios';
import NavBar from '../components/NavBar';

const DashboardPage = () => {
  const [tasks, setTasks] = useState([]);
  const [messages, setMessages] = useState([]);
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [tasksRes, messagesRes, usersRes] = await Promise.all([
          axios.get('/kanban/task'),
          axios.get('/chat/messages'),
          axios.get('/users')
        ]);

        setTasks(tasksRes.data);
        setMessages(messagesRes.data);
        setUsers(usersRes.data);
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const prepareChartData = () => {
    const messagesData = users.map(user => {
      const userMessages = messages.filter(msg =>
        String(msg.senderName).toLowerCase() === String(user.username).toLowerCase()
      );
      return {
        name: user.username,
        messages: userMessages.length
      };
    }).filter(item => item.messages > 0);

    const tasksData = users.map(user => {
      const userTasks = tasks.filter(task => task.ownerId === user.id);
      return {
        name: user.username,
        tasks: userTasks.length
      };
    }).filter(item => item.tasks > 0);

    const lastStatus = tasks.reduce((acc, task) => {
      if (!acc || task.status.id > acc.id) {
        return task.status;
      }
      return acc;
    }, null);

    const profitData = users.map(user => {
      const userTasks = tasks.filter(
        task => task.ownerId === user.id && task.status.id === lastStatus?.id
      );
      const totalProfit = userTasks.reduce((sum, task) => sum + (task.profit || 0), 0);
      return {
        name: user.username,
        profit: totalProfit
      };
    }).filter(item => item.profit > 0);

    return { messagesData, tasksData, profitData };
  };

  const { messagesData, tasksData, profitData } = prepareChartData();

  const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884D8'];

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
          <Grid2 xs={12}>
            <Paper elevation={3} sx={{ p: 2, height: '100%', display: 'flex', flexDirection: 'column' }}>
              <Typography variant="h6" gutterBottom>
                Количество сообщений для каждого пользователя.
              </Typography>
              <Box sx={{ flexGrow: 1 }}>
                <ResponsiveContainer width="100%" height={400}>
                  <BarChart data={messagesData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip />
                    <Legend />
                    <Bar dataKey="messages" fill="#8884d8" name="Сообщения" />
                  </BarChart>
                </ResponsiveContainer>
              </Box>
            </Paper>
          </Grid2>
          <Grid2 xs={12}>
            <Paper elevation={3} sx={{ p: 2, height: '100%', display: 'flex', flexDirection: 'column' }}>
              <Typography variant="h6" gutterBottom>
                Количество сделок, созданных каждым пользователем.
              </Typography>
              <Box sx={{ flexGrow: 1 }}>
                <ResponsiveContainer width="100%" height={400}>
                  <PieChart>
                    <Pie
                      data={tasksData}
                      cx="50%"
                      cy="50%"
                      labelLine={false}
                      innerRadius={60}
                      outerRadius={150}
                      paddingAngle={1}
                      cornerRadius={5}
                      fill="#8884d8"
                      dataKey="tasks"
                      nameKey="name"
                      label={({ name, percent }) =>
                        `${name}: ${(percent * 100).toFixed(0)}%`
                      }
                    >
                      {tasksData.map((entry, index) => (
                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                      ))}
                    </Pie>
                    <Tooltip formatter={(value) => [`${value} tasks`, 'Count']} />
                    <Legend />
                  </PieChart>
                </ResponsiveContainer>
              </Box>
            </Paper>
          </Grid2>
          <Grid2 xs={12}>
            <Paper elevation={3} sx={{ p: 2, height: '100%', display: 'flex', flexDirection: 'column' }}>
              <Typography variant="h6" gutterBottom>
                Прибыль, которую принёс каждый сотрудник.
              </Typography>
              <Box sx={{ flexGrow: 1 }}>
                <ResponsiveContainer width="100%" height={400}>
                  <BarChart data={profitData}>
                    <CartesianGrid strokeDasharray="3 3" />
                    <XAxis dataKey="name" />
                    <YAxis />
                    <Tooltip formatter={(value) => [`${value} ₽`, 'Revenue']} />
                    <Legend />
                    <Bar dataKey="profit" fill="#82ca9d" name="Прибыль (₽)" />
                  </BarChart>
                </ResponsiveContainer>
              </Box>
            </Paper>
          </Grid2>
        </Grid2>
      </Box>
    </>
  );
};

export default DashboardPage;
