// src/main/resources/minifront/src/pages/LoginPage.jsx
import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import {
	Box,
	TextField,
	Button,
	Typography,
	Paper,
	Alert,
	Link,
	Container,
} from "@mui/material";
import { authService } from "../services/authService";

const LoginPage = () => {
	const navigate = useNavigate();
	const location = useLocation();
	const [username, setUsername] = useState("");
	const [password, setPassword] = useState("");
	const [error, setError] = useState("");
	const [loading, setLoading] = useState(false);

	const handleLogin = async (e) => {
		e.preventDefault();
		setLoading(true);
		setError("");

		try {
			console.log("Отправка запроса на вход...");
			const data = await authService.login(username, password);
			console.log("Получен ответ:", data);

			if (data.token) {
				localStorage.setItem("token", data.token);
				localStorage.setItem("username", username);
				const from = location.state?.from || "/chat";
				navigate(from, { replace: true });
			} else {
				setError("Неверный формат ответа от сервера");
			}
		} catch (err) {
			console.error("Ошибка при входе:", err);
			setError(err.message || "Неверное имя пользователя или пароль");
		} finally {
			setLoading(false);
		}
	};

	const handleRegisterClick = (e) => {
		e.preventDefault();
		navigate("/register");
	};

	return (
		<Container component="main" maxWidth="xs">
			<Box
				sx={{
					marginTop: 8,
					display: "flex",
					flexDirection: "column",
					alignItems: "center",
				}}
			>
				<Paper
					elevation={3}
					sx={{
						p: 4,
						width: "100%",
						display: "flex",
						flexDirection: "column",
						alignItems: "center",
					}}
				>
					<Typography component="h1" variant="h5">
						Вход в систему
					</Typography>

					{error && (
						<Alert severity="error" sx={{ mt: 2, width: "100%" }}>
							{error}
						</Alert>
					)}

					<Box
						component="form"
						onSubmit={handleLogin}
						sx={{ mt: 1, width: "100%" }}
					>
						<TextField
							margin="normal"
							required
							fullWidth
							id="username"
							label="Имя пользователя"
							name="username"
							autoComplete="username"
							autoFocus
							value={username}
							onChange={(e) => setUsername(e.target.value)}
						/>
						<TextField
							margin="normal"
							required
							fullWidth
							name="password"
							label="Пароль"
							type="password"
							id="password"
							autoComplete="current-password"
							value={password}
							onChange={(e) => setPassword(e.target.value)}
						/>
						<Button
							type="submit"
							fullWidth
							variant="contained"
							sx={{ mt: 3, mb: 2 }}
							disabled={loading}
						>
							{loading ? "Вход..." : "Войти"}
						</Button>
						<Box sx={{ textAlign: "center" }}>
							<Link
								component="button"
								variant="body2"
								onClick={handleRegisterClick}
							>
								{"Нет аккаунта? Зарегистрироваться"}
							</Link>
						</Box>
					</Box>
				</Paper>
			</Box>
		</Container>
	);
};

export default LoginPage;
