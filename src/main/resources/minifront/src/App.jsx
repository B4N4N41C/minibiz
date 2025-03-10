import { Routes, Route } from 'react-router-dom';
import RegisterForm from './RegisterForm';
import LoginForm from './LoginForm';

function App() {
	return (
		<div className="App">
			<Routes>
				<Route path="/" element={<RegisterForm />} />
				<Route path="/login" element={<LoginForm />} />
			</Routes>
		</div>
	);
}

export default App;