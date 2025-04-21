const API_URL = "http://localhost:8080";

export const authService = {
	async login(username, password) {
		try {
			console.log("Отправка запроса на:", `${API_URL}/auth/sign-in`);
			const response = await fetch(`${API_URL}/auth/sign-in`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json",
				},
				body: JSON.stringify({ username, password }),
			});

			if (!response.ok) {
				const errorData = await response.json();
				throw new Error(errorData.message || "Ошибка авторизации");
			}

			const data = await response.json();
			console.log("Ответ сервера:", data);
			return data;
		} catch (error) {
			console.error("Ошибка в authService.login:", error);
			throw error;
		}
	},

	async register(username, email, password) {
		const response = await fetch(`${API_URL}/auth/sign-up`, {
			method: "POST",
			headers: {
				"Content-Type": "application/json",
			},
			body: JSON.stringify({ username, email, password }),
		});

		if (!response.ok) {
			throw new Error("Ошибка регистрации");
		}

		return response.json();
	},

	logout(navigate) {
		localStorage.removeItem("token");
		localStorage.removeItem("username");
		navigate("/login");
	},
};
