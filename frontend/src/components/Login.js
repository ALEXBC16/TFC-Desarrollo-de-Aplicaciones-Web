import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import '../css/Login.css';
import '../css/Global.css';

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

      const {
        token,
        nombreUsuario: usuarioNombre,
        rol,
        idUsuario,
        tipoSuscripcion
      } = response.data;

      // Guardar datos en localStorage
      localStorage.setItem("token", token);
      localStorage.setItem("rol", rol);
      localStorage.setItem("nombreUsuario", usuarioNombre);
      localStorage.setItem("idUsuario", idUsuario);
      localStorage.setItem("tipoSuscripcion", tipoSuscripcion);

      // Verificación por consola después de guardar
      console.log("LOGIN DATA:", response.data);
      console.log("TOKEN GUARDADO: ", localStorage.getItem("token"));
      console.log("ROL GUARDADO: ", localStorage.getItem("rol"));

      onLogin({ nombreUsuario: usuarioNombre });

      // Redirigir según tipo de suscripción
      if (parseInt(tipoSuscripcion) === 4) {
        navigate('/admin-especial');
      } else {
        navigate('/home');
      }

    } catch (err) {
      setError('Usuario o contraseña incorrectos');
      console.error(err);
    }
  };

  return (
    <>
      <Header />
      <div className="login-container">
        <div className="login-box">
          <h2>Iniciar Sesión</h2>
          <form onSubmit={handleSubmit}>
            <input
              type="text"
              placeholder="Nombre de usuario"
              value={nombreUsuario}
              onChange={(e) => setNombreUsuario(e.target.value)}
              required
            />
            <input
              type="password"
              placeholder="Contraseña"
              value={contrasena}
              onChange={(e) => setContrasena(e.target.value)}
              required
            />
            <button type="submit">Entrar</button>
          </form>

          {error && <p style={{ color: 'red' }}>{error}</p>}

          <p>¿No tienes cuenta?</p>
          <button onClick={() => navigate('/register')}>
            Regístrate
          </button>
        </div>
      </div>
      <Footer />
    </>
  );
}

export default Login;
