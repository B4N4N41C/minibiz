import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
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

const RegisterPage = () => {
	const [username, setUsername] = useState("");
	const [email, setEmail] = useState("");
	const [password, setPassword] = useState("");
	const [error, setError] = useState("");
	const navigate = useNavigate();

	const handleRegister = async (e) => {
		e.preventDefault();
		try {
			const response = await fetch("http://localhost:8080/auth/sign-up", {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ username, email, password }),
			});

			if (!response.ok) {
				throw new Error("Ошибка регистрации");
			}

			const data = await response.json();
			localStorage.setItem("token", data.token);
			localStorage.setItem("username", username);
			navigate("/chat");
		} catch (err) {
			console.log(err);
			setError("Ошибка при регистрации");
		}
	};

	const handleLoginClick = (e) => {
		e.preventDefault();
		navigate("/login");
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
						Регистрация
					</Typography>

					{error && (
						<Alert severity="error" sx={{ mt: 2, width: "100%" }}>
							{error}
						</Alert>
					)}

					<Box
						component="form"
						onSubmit={handleRegister}
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
							id="email"
							label="Email"
							name="email"
							autoComplete="email"
							value={email}
							onChange={(e) => setEmail(e.target.value)}
						/>
						<TextField
							margin="normal"
							required
							fullWidth
							name="password"
							label="Пароль"
							type="password"
							id="password"
							autoComplete="new-password"
							value={password}
							onChange={(e) => setPassword(e.target.value)}
						/>
						<Button
							type="submit"
							fullWidth
							variant="contained"
							sx={{ mt: 3, mb: 2 }}
						>
							Зарегистрироваться
						</Button>
						<Box sx={{ textAlign: "center" }}>
							<Link
								component="button"
								variant="body2"
								onClick={handleLoginClick}
							>
								{"Уже есть аккаунт? Войти"}
							</Link>
						</Box>
					</Box>
				</Paper>
			</Box>
		</Container>
	);
};

export default RegisterPage;
