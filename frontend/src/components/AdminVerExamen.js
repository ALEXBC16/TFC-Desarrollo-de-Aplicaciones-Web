import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useParams, useNavigate } from 'react-router-dom';
import Header from './Header';
import Footer from './Footer';

const AdminVerExamen = () => {
  const { idExamen } = useParams();
  const [preguntas, setPreguntas] = useState([]);
  const [mensaje, setMensaje] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchPreguntasYRespuestas = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get(`http://localhost:8080/api/preguntas/examen-con-respuestas/${idExamen}`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        setPreguntas(response.data);
      } catch (err) {
        console.error('Error al cargar preguntas con respuestas', err);
        setMensaje('No se pudieron cargar las preguntas y respuestas.');
      }
    };

    fetchPreguntasYRespuestas();
  }, [idExamen]);

  return (
    <>
      <Header />
      <div className="admin-examen-detalle">
        <h2>Preguntas del Examen {idExamen}</h2>
        {mensaje && <p>{mensaje}</p>}

        <ul>
          {preguntas.map((pregunta) => (
            <li key={pregunta.idPregunta}>
              <strong>{pregunta.enunciado}</strong>
              <ul>
                {pregunta.respuestas.map((resp) => (
                  <li key={resp.idRespuesta}>
                    {resp.respuesta} {resp.esCorrecta ? '✅' : ''}
                  </li>
                ))}
              </ul>
            </li>
          ))}
        </ul>

        <button onClick={() => navigate('/admin-especial')} style={{ marginTop: '20px' }}>
          Volver
        </button>
      </div>
      <Footer />
    </>
  );
};

export default AdminVerExamen;
