import AppBar from "@mui/material/AppBar";
import { Box, Button, Drawer, IconButton, Toolbar } from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import ChatIcon from "@mui/icons-material/Chat";
import ViewKanbanIcon from "@mui/icons-material/ViewKanban";
import LogoutIcon from "@mui/icons-material/Logout";
import EqualizerIcon from '@mui/icons-material/Equalizer';
import PeopleIcon from '@mui/icons-material/People';
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { authService } from "../services/authService";

const NavBar = () => {
	const [open, setOpen] = useState(false);
	const navigate = useNavigate();

	const handleKanbanRedirect = () => {
		navigate("/kanban");
	};
	const handleChatRedirect = () => {
		navigate("/chat");
	};

	const handleUsersRedirect = () => {
		navigate("/users");
	};

	const handleLogout = () => {
		authService.logout(navigate);
	};

	const handleDashboardRedirect = () => {
		navigate("/dashboard");
	};

	const toggleDrawer = (newOpen) => () => {
		setOpen(newOpen);
	};
	return (
		<>
			<AppBar
				position="static"
				color="primary"
				enableColorOnDark
				sx={{ mb: 2 }}
			>
				<Toolbar variant="dense">
					<IconButton
						edge="start"
						color="inherit"
						aria-label="menu"
						onClick={toggleDrawer(true)}
					>
						<MenuIcon />
					</IconButton>
					<Box sx={{ flexGrow: 1 }} />
					<IconButton color="inherit" onClick={handleLogout} edge="end">
						<LogoutIcon />
					</IconButton>
				</Toolbar>
			</AppBar>
			<Drawer open={open} onClose={toggleDrawer(false)}>
				<Box sx={{ minWidth: 220 }}>
					<Button
						onClick={() => handleDashboardRedirect()}
						variant="text"
						startIcon={<EqualizerIcon />}
						sx={{ minWidth: 1 / 1 }}
					>
						Статистика
					</Button>
					<Button
						onClick={() => handleKanbanRedirect()}
						variant="text"
						startIcon={<ViewKanbanIcon />}
						sx={{ minWidth: 1 / 1, mb: 1 }}
					>
						Канбан доска
					</Button>
					<Button
						onClick={() => handleChatRedirect()}
						variant="text"
						startIcon={<ChatIcon />}
						sx={{ minWidth: 1 / 1 }}
					>
						Чат
					</Button>
					<Button
						onClick={() => handleUsersRedirect()}
						variant="text"
						startIcon={<PeopleIcon />}
						sx={{ minWidth: 1 / 1 }}
					>
						Пользователи
					</Button>
				</Box>
			</Drawer>
		</>
	);
};

export default NavBar;
