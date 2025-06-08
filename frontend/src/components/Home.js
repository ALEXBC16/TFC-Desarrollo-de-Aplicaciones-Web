import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Header from '../components/Header';
import Footer from '../components/Footer';
import '../css/Home.css';
import '../css/Global.css';

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
    <>
      <Header />
      <div className="home-container">
        <h2>Bienvenido, {usuario?.nombreUsuario}</h2>

        <div className="home-content">
          {/* Columna izquierda: Exámenes */}
          <div className="home-left">
            <h3 className="section-title">Exámenes disponibles</h3>
            {Object.entries(examenesPorNivel).map(([nivel, lista]) => (
              <div key={nivel} className="nivel-section">
                <h4>{nivel}</h4>
                {lista.length === 0 ? (
                  <p className="no-exams">No hay exámenes de este nivel.</p>
                ) : (
                  <div className="button-grid">
                    {lista.map(examen => (
                      <button key={examen.idExamen} onClick={() => handleIrExamen(examen.idExamen)}>
                        {examen.nombre}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            ))}
          </div>

          {/* Columna derecha: Resultados */}
          <div className="home-right">
            <h3 className="section-title">Últimos exámenes realizados</h3>
            {resultados.length === 0 ? (
              <p>No hay exámenes registrados aún.</p>
            ) : (
              <table className="exam-table">
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
        </div>
        <div className="logout-wrapper">
          <button className="logout-button" onClick={handleLogout}>
            Cerrar sesión
          </button>
        </div>
      </div>
      <Footer />

    </>
  );
}

export default Home;
