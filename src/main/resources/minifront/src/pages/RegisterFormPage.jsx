import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button, Container, TextField } from '@mui/material'
import EmailOutlinedIcon from '@mui/icons-material/EmailOutlined';

const RegisterFormPage = () => {
  const [formData, setFormData] = useState({
    username: '',
    password: '',
    email: ''
  })

  const navigate = useNavigate()

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch('http://localhost:8080/auth/sign-up', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      })

      if (response.ok) {
        const data = await response.json()
        localStorage.setItem('token', data.token)
        alert('Registration successful!')
      } else {
        alert('Registration failed!')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('An error occurred during registration.')
    }
  }

  const handleLoginRedirect = () => {
    navigate('/login')
  }

  return (
    <Container maxWidth="sm">
      <form onSubmit={handleSubmit}>
        <TextField
          className="w-full"
          type="text"
          name="username"
          label="Username"
          value={formData.username}
          onChange={handleChange}
          sx={{ margin: 2, width: 1 }}
          required
        />
        <TextField
          className="w-full mb-10"
          type="email"
          name="email"
          label="Email"
          value={formData.email}
          onChange={handleChange}
          sx={{ margin: 2, width: 1 }}
          required
        />
        <TextField
          className="w-full mb-10"
          type="password"
          label="Password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          sx={{ margin: 2, width: 1 }}
          required
        />
        <Button variant="contained" type="submit" sx={{ margin: 2, width: 1 }}>Register</Button>
        <Button variant="outlined" onClick={handleLoginRedirect} sx={{ margin: 2, width: 1 }}>
          Go to Login
        </Button>
      </form>
    </Container>
  )
}

export default RegisterFormPage