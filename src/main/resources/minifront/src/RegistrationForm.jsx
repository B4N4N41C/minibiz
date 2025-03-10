import React, { useState } from 'react';
import axios from 'axios';

const RegistrationForm = () => {
  // Состояния для данных формы и сообщений
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
  });
  const [message, setMessage] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    console.log('Данные формы перед отправкой:', formData);

    try {
      const response = await axios.post('http://localhost:8080/user/register', formData);

      setMessage('Регистрация прошла успешно!');
      console.log('Ответ сервера:', response.data);

      setFormData({
        username: '',
        email: '',
        password: '',
      });
    } catch (error) {
      // Обработка ошибки
      console.error('Ошибка при регистрации:', error);
      setMessage('Ошибка при регистрации!');
    }
  };

  return (
    <div className="registration-form">
      <h2>Регистрация</h2>
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label htmlFor="username">Имя пользователя:</label>
          <input
            type="text"
            id="username"
            name="username"
            value={formData.username}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="email">Электронная почта:</label>
          <input
            type="email"
            id="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label htmlFor="password">Пароль:</label>
          <input
            type="password"
            id="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit">Зарегистрироваться</button>
      </form>

      {message && <div className="message">{message}</div>}
    </div>
  );
};

export default RegistrationForm;