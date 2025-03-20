import {Routes, Route } from 'react-router-dom'
import RegisterFormPage from './pages/RegisterFormPage.jsx'
import LoginFormPage from './pages/LoginFormPage.jsx'
import KanbanBoardPage from './pages/KanbanBoardPage.jsx'
import './style/App.css'

function App() {
  return (
    <div className="App">
      <Routes>
          <Route path="/" element={<RegisterFormPage />} />
          <Route path="/login" element={<LoginFormPage />} />
          <Route path="/kanban" element={<KanbanBoardPage />} />
      </Routes>
    </div>
  )
}

export default App