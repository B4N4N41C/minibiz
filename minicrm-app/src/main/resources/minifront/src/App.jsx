import {Routes, Route, Router, Navigate} from "react-router-dom";
import RegisterPage from "./pages/RegisterFormPage.jsx";
import LoginPage from "./pages/LoginFormPage.jsx";
import KanbanBoardPage from "./pages/KanbanBoardPage.jsx";
import ChatPage from "./pages/ChatPage.jsx";
import PrivateRoute from "./components/PrivateRoute.jsx";
import UsersPage from './pages/UsersPage.jsx'
import DashboardPage from './pages/DashboardPage.jsx'

function App() {
    return (
        <>
            <Routes>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/users" element={<UsersPage/>}/>
                <Route path="/dashboard" element={<DashboardPage/>}/>

                {/*<Route path="/statistics" element={<StatisticsPage />} />*/}
                <Route
                    path="/chat"
                    element={
                        <ChatPage/>
                    }
                />
                <Route
                    path="/kanban"
                    element={
                        <KanbanBoardPage/>
                    }
                />

                <Route path="/" element={<Navigate to="/kanban" replace/>}/>
            </Routes>
        </>
    );
}

export default App;
