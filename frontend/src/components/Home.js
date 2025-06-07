import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

function Home() {
  const navigate = useNavigate();
  const [usuario, setUsuario] = useState(null);
  const [resultados, setResultados] = useState([]);
  const [examenes, setExamenes] = useState([]);

  useEffect(() => {
    const nombreUsuario = localStorage.getItem('nombreUsuario');
    const token = localStorage.getItem('token');

    if (!token || !nombreUsuario) {
      navigate('/');
      return;
    }

    axios.get(`http://localhost:8080/api/usuarios/nombre/${nombreUsuario}`, {
      headers: { Authorization: `Bearer ${token}` }
    })
      .then(res => {
        setUsuario(res.data);
        return Promise.all([
          axios.get(`http://localhost:8080/api/usuarios-examenes/ultimos/${res.data.idUsuario}`, {
            headers: { Authorization: `Bearer ${token}` }
          }),
          axios.get(`http://localhost:8080/api/examenes`, {
            headers: { Authorization: `Bearer ${token}` }
          })
        ]);
      })
      .then(([resResultados, resExamenes]) => {
        setResultados(resResultados.data);
        setExamenes(resExamenes.data);
      })
      .catch(err => {
        console.error('Error al cargar datos del usuario:', err);
        navigate('/');
      });
  }, [navigate]);

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const handleIrExamen = (idExamen) => {
    navigate(`/examen/${idExamen}`);
  };

  const filtrarExamenesPorSuscripcion = () => {
    if (!usuario) return [];

    if (usuario.tipoSuscripcion === 0) {
      return examenes;
    } else if (usuario.tipoSuscripcion === 1) {
      return examenes.filter(e => e.nombre.toLowerCase().includes("coche"));
    } else if (usuario.tipoSuscripcion === 2) {
      return examenes.filter(e => e.nombre.toLowerCase().includes("moto"));
    }
    return [];
  };

  const examenesFiltrados = filtrarExamenesPorSuscripcion();

  const examenesPorNivel = {
    Iniciacion: examenesFiltrados.filter(e => e.nivel === 'Iniciacion'),
    Medio: examenesFiltrados.filter(e => e.nivel === 'Medio'),
    Avanzado: examenesFiltrados.filter(e => e.nivel === 'Avanzado')
  };

  return (
    <div>
      <h2>Bienvenido, {usuario?.nombreUsuario}</h2>
      <p>Tu tipo de suscripción es: {usuario?.tipoSuscripcion}</p>

      <button onClick={handleLogout}>Cerrar sesión</button>

      <h3 style={{ marginTop: '30px' }}>Exámenes disponibles</h3>

      {Object.entries(examenesPorNivel).map(([nivel, lista]) => (
        <div key={nivel} style={{ marginBottom: '20px' }}>
          <h4>{nivel}</h4>
          {lista.length === 0 ? (
            <p style={{ fontStyle: 'italic' }}>No hay exámenes de este nivel.</p>
          ) : (
            lista.map(examen => (
              <div key={examen.idExamen} style={{ margin: '5px 0' }}>
                <button onClick={() => handleIrExamen(examen.idExamen)}>
                  {examen.nombre}
                </button>
              </div>
            ))
          )}
        </div>
      ))}

      <h3 style={{ marginTop: '40px' }}>Últimos 5 exámenes realizados</h3>
      {resultados.length === 0 ? (
        <p>No hay exámenes registrados aún.</p>
      ) : (
        <table border="1" cellPadding="10">
          <thead>
            <tr>
              <th>Fecha</th>
              <th>Hora</th>
              <th>Examen</th>
              <th>Nota</th>
            </tr>
          </thead>
          <tbody>
            {resultados.map((res, i) => {
              const fecha = new Date(res.fechaRealizacion);
              return (
                <tr key={i}>
                  <td>{fecha.toLocaleDateString()}</td>
                  <td>{fecha.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</td>
                  <td>{res.examen?.nombre || 'Desconocido'}</td>
                  <td>{res.nota}</td>
                </tr>
              );
            })}
          </tbody>
        </table>
      )}
    </div>
  );
}

export default Home;
