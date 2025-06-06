import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Home() {
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState(null);

  useEffect(() => {
    const nombreUsuario = localStorage.getItem('nombreUsuario');
    const token = localStorage.getItem('token');

    if (!token || !nombreUsuario) {
      navigate('/');
      return;
    }

    axios.get(`http://localhost:8080/api/usuarios/nombre/${nombreUsuario}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
      .then(res => setUsuario(res.data))
      .catch(err => {
        console.error('Error al recuperar el usuario:', err);
        navigate('/');
      });
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('nombreUsuario');
    navigate('/');
  };

  const renderBotonSegunTipo = () => {
    if (usuario.tipoSuscripcion === 0) {
      return <button onClick={() => navigate('/admin')}>Añadir preguntas</button>;
    } else if (usuario.tipoSuscripcion === 1) {
      return <button onClick={() => navigate('/test-coche')}>Ir al examen de coche</button>;
    } else if (usuario.tipoSuscripcion === 2) {
      return <button onClick={() => navigate('/test-moto')}>Ir al examen de moto</button>;
    } else {
      return null;
    }
  };

  if (!usuario) return <p>Cargando usuario...</p>;

  return (
    <div>
      <h2>Bienvenido, {usuario.nombreUsuario}</h2>
      <p>Tu tipo de suscripción es: {usuario.tipoSuscripcion}</p>

      {renderBotonSegunTipo()}

      <button onClick={handleLogout}>Cerrar sesión</button>
    </div>
  );
}

export default Home;
