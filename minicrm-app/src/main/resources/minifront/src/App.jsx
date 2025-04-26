import { Routes, Route, Router, Navigate } from "react-router-dom";
import RegisterPage from "./pages/RegisterFormPage.jsx";
import LoginPage from "./pages/LoginFormPage.jsx";
import KanbanBoardPage from "./pages/KanbanBoardPage.jsx";
import ChatPage from "./pages/ChatPage.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
function App() {
	// return (
	// 	<div className="App">
	// 		<Routes>
	// 			<Route path="/" element={<RegisterFormPage />} />
	// 			<Route path="/login" element={<LoginFormPage />} />
	// 			<Route path="/kanban" element={<KanbanBoardPage />} />
	// 			<Route path="/chat" element={<ChatPage />} />
	// 		</Routes>
	// 	</div>
	// );
	return (
		<>
			<Routes>
				<Route path="/login" element={<LoginPage />} />
				<Route path="/register" element={<RegisterPage />} />
				{/*<Route path="/statistics" element={<StatisticsPage />} />*/}
				<Route
					path="/chat"
					element={
						<PrivateRoute>
							<ChatPage />
						</PrivateRoute>
					}
				/>
				<Route
					path="/kanban"
					element={
						<PrivateRoute>
							<KanbanBoardPage />
						</PrivateRoute>
					}
				/>
				<Route path="/" element={<Navigate to="/chat" replace />} />
			</Routes>
		</>
	);
}

export default App;
