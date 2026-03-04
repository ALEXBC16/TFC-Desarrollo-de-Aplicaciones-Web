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

    axios.get(
      `${process.env.REACT_APP_API_URL}/api/usuarios/nombre/${nombreUsuario}`,
      { headers: { Authorization: `Bearer ${token}` } }
    )
      .then(res => {
        setUsuario(res.data);

        return Promise.all([
          axios.get(
            `${process.env.REACT_APP_API_URL}/api/usuarios-examenes/ultimos/${res.data.idUsuario}`,
            { headers: { Authorization: `Bearer ${token}` } }
          ),
          axios.get(
            `${process.env.REACT_APP_API_URL}/api/examenes`,
            { headers: { Authorization: `Bearer ${token}` } }
          )
        ]);
      })
      .then(([resResultados, resExamenes]) => {
        setResultados(resResultados.data);
        setExamenes(resExamenes.data);
      })
      .catch(() => navigate('/'));
  }, [navigate]);

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const handleIrExamen = (idExamen) => {
    navigate(`/examen/${idExamen}`);
  };

  // TEST ALEATORIO
  const handleTestAleatorio = async () => {
    try {
      const token = localStorage.getItem("token");

      const response = await axios.get(
        `${process.env.REACT_APP_API_URL}/api/preguntas/test-aleatorio?cantidad=30`,
        {
          headers: {
            Authorization: `Bearer ${token}`
          }
        }
      );

      navigate("/examen/aleatorio", {
        state: { preguntas: response.data }
      });

    } catch (error) {
      console.error("Error generando test aleatorio:", error);
    }
  };

  const filtrarExamenesPorSuscripcion = () => {
    if (!usuario) return [];

    // admin o premium
    if (usuario.tipoSuscripcion === 0 || usuario.tipoSuscripcion === 4) {
      return examenes;
    }

    // coche
    if (usuario.tipoSuscripcion === 1) {
      return examenes.filter(e => e.categoria === 1);
    }

    // moto
    if (usuario.tipoSuscripcion === 2) {
      return examenes.filter(e => e.categoria === 2);
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

          {/* IZQUIERDA */}
          <div className="home-left">
            <h3 className="section-title">Exámenes disponibles</h3>

            {Object.entries(examenesPorNivel).map(([nivel, lista]) => (
              <div key={nivel} className="nivel-section">
                <h4>{nivel}</h4>

                {lista.length === 0 ? (
                  <p className="no-exams">
                    No hay exámenes de este nivel.
                  </p>
                ) : (
                  <div className="button-grid">
                    {lista.map(examen => (
                      <button
                        key={examen.idExamen}
                        onClick={() => handleIrExamen(examen.idExamen)}
                      >
                        {examen.nombre}
                      </button>
                    ))}
                  </div>
                )}
              </div>
            ))}

            {/* BOTÓN TEST ALEATORIO */}
            <div className="test-aleatorio-wrapper">
              <button
                className="test-aleatorio-button"
                onClick={handleTestAleatorio}
              >
                🎲 Test Aleatorio
              </button>
            </div>

          </div>

          {/* DERECHA */}
          <div className="home-right">
            <h3 className="section-title">
              Últimos exámenes realizados
            </h3>

            {resultados.length === 0 ? (
              <p className="no-results">
                No has realizado ningún examen todavía.
              </p>
            ) : (
              <table className="tabla-resultados">
                <thead>
                  <tr>
                    <th>Fecha</th>
                    <th>Hora</th>
                    <th>Examen</th>
                    <th>Nota</th>
                  </tr>
                </thead>

                <tbody>
                  {resultados.map((r) => {
                    const fecha = new Date(r.fechaRealizacion);

                    return (
                      <tr key={r.id}>
                        <td>{fecha.toLocaleDateString()}</td>
                        <td>{fecha.toLocaleTimeString()}</td>
                        <td>{r.examen ? r.examen.nombre : "Examen aleatorio"}</td>
                        <td>{r.nota}</td>
                      </tr>
                    );
                  })}
                </tbody>

              </table>
            )}
          </div>

        </div>

        <div className="logout-wrapper">
          <button
            className="logout-button"
            onClick={handleLogout}
          >
            Cerrar sesión
          </button>
        </div>

      </div>

      <Footer />
    </>
  );
}

export default Home;