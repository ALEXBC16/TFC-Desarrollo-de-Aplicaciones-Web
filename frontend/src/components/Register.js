import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Register = () => {
  const [formData, setFormData] = useState({
    nombreUsuario: '',
    contrasenaUsuario: '',
    fotoPerfil: '',
    tipoSuscripcion: 1,
  });

  const [mensaje, setMensaje] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      // 1. Crear usuario
      const response = await axios.post('http://localhost:8080/api/usuarios', formData);
      const nombreUsuario = response.data.nombreUsuario;

      // 2. Hacer login automáticamente (cambiado: contrasenaUsuario)
      const loginResponse = await axios.post('http://localhost:8080/api/auth/login', {
        nombreUsuario: nombreUsuario,
        contrasenaUsuario: formData.contrasenaUsuario
      });

      // 3. Guardar token y nombre en localStorage
      localStorage.setItem('token', loginResponse.data.token);
      localStorage.setItem('nombreUsuario', nombreUsuario);

      // 4. Redirigir al home
      navigate('/home');
    } catch (error) {
      console.error(error);
      setMensaje('Error al registrar o iniciar sesión.');
    }
  };

  return (
    <div>
      <h2>Registro de Usuario</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="nombreUsuario"
          placeholder="Nombre de usuario"
          value={formData.nombreUsuario}
          onChange={handleChange}
          required
        />
        <br />
        <input
          type="password"
          name="contrasenaUsuario"
          placeholder="Contraseña"
          value={formData.contrasenaUsuario}
          onChange={handleChange}
          required
        />
        <br />
        <input
          type="text"
          name="fotoPerfil"
          placeholder="URL de foto de perfil"
          value={formData.fotoPerfil}
          onChange={handleChange}
        />
        <br />
        <select
          name="tipoSuscripcion"
          value={formData.tipoSuscripcion}
          onChange={handleChange}
        >
          <option value={0}>Superusuario</option>
          <option value={1}>Usuario Coche</option>
          <option value={2}>Usuario Moto</option>
        </select>
        <br />
        <button type="submit">Registrarse</button>
      </form>

      {mensaje && <p>{mensaje}</p>}

      <button onClick={() => navigate('/')}>Volver a Iniciar Sesión</button>
    </div>
  );
};

export default Register;
