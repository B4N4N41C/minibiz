import React, { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { Button, Container, TextField } from '@mui/material'

const LoginFormPage = () => {
  const navigate = useNavigate()

  const [formData, setFormData] = useState({
    username: '',
    password: ''
  })

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    })
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    try {
      const response = await fetch('http://localhost:8080/auth/sign-in', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
      })

      if (response.ok) {
        const data = await response.json()
        localStorage.setItem('token', data.token)
        alert('Login sussfull!')
        navigate('/kanban')
      } else {
        alert('Login failed!')
      }
    } catch (error) {
      console.error('Error:', error)
      alert('An error occurred during login.')
    }
  }

  const handleRegisterRedirect = () => {
    navigate('/')
  }

  return (
    <Container maxWidth="sm">
      <form onSubmit={handleSubmit}>
        <TextField
          type="text"
          label="Username"
          name="username"
          value={formData.username}
          onChange={handleChange}
          sx={{ margin: 2, width: 1 }}
          required
        />
        <TextField
          type="password"
          label="Password"
          name="password"
          value={formData.password}
          onChange={handleChange}
          sx={{ margin: 2, width: 1 }}
          required
        />
        <Button variant="contained" type="submit" sx={{ margin: 2, width: 1 }}>Login</Button>
        <Button variant="outlined" onClick={handleRegisterRedirect} sx={{ margin: 2, width: 1 }}>
          Go to Register
        </Button>
      </form>
    </Container>
  )
}

export default LoginFormPage