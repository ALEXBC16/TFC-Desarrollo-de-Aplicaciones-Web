import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function Login({ onLogin }) {
  const [nombreUsuario, setNombreUsuario] = useState('');
  const [contrasena, setContrasena] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await axios.post('http://localhost:8080/api/auth/login', {
        nombreUsuario,
        contrasenaUsuario: contrasena
      });

      const { token, nombreUsuario: usuarioNombre } = response.data;

      // Guardar token en localStorage
      localStorage.setItem('token', token);
      localStorage.setItem('nombreUsuario', usuarioNombre);

      // Puedes guardar más datos si los necesitas
      onLogin({ nombreUsuario: usuarioNombre });
      navigate('/home');
    } catch (err) {
      setError('Usuario o contraseña incorrectos');
      console.error(err);
    }
  };

  return (
    <div>
      <h2>Iniciar Sesión</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Nombre de usuario"
          value={nombreUsuario}
          onChange={(e) => setNombreUsuario(e.target.value)}
          required
        />
        <br />
        <input
          type="password"
          placeholder="Contraseña"
          value={contrasena}
          onChange={(e) => setContrasena(e.target.value)}
          required
        />
        <br />
        <button type="submit">Entrar</button>
      </form>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <p>¿No tienes cuenta?</p>
      <button onClick={() => navigate('/register')}>
        Regístrate
      </button>
    </div>
  );
}

export default Login;
