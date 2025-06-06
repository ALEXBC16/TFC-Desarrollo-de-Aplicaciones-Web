import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

function TestCoche() {
  const [preguntas, setPreguntas] = useState([]);
  const [respuestasSeleccionadas, setRespuestasSeleccionadas] = useState({});
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem('token');

    axios.get('http://localhost:8080/api/preguntas/examen/1', {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(res => {
      console.log('Preguntas recibidas:', res.data);
      setPreguntas(res.data);
    })
    .catch(err => {
      console.error('Error al cargar preguntas:', err);
    });
  }, []);

  const handleSeleccionRespuesta = (idPregunta, idRespuesta) => {
    setRespuestasSeleccionadas(prev => ({
      ...prev,
      [idPregunta]: idRespuesta
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log('Respuestas enviadas:', respuestasSeleccionadas);
    alert('Test enviado. (Falta implementar la corrección)');
  };

  const volverAlHome = () => {
    setRespuestasSeleccionadas({});
    setPreguntas([]);
    navigate('/home');
  };

  return (
    <div>
      <h2>Test Teórico de Coche</h2>
      <form onSubmit={handleSubmit}>
        {preguntas.map(pregunta => (
          <div key={pregunta.idPregunta} style={{ marginBottom: '1rem' }}>
            <strong>{pregunta.enunciado}</strong>
            <div>
              {Array.isArray(pregunta.respuestas) && pregunta.respuestas.length > 0 ? (
                pregunta.respuestas.map(respuesta => (
                  <label key={respuesta.idRespuesta} style={{ display: 'block' }}>
                    <input
                      type="radio"
                      name={`pregunta-${pregunta.idPregunta}`}
                      value={respuesta.idRespuesta}
                      checked={respuestasSeleccionadas[pregunta.idPregunta] === respuesta.idRespuesta}
                      onChange={() => handleSeleccionRespuesta(pregunta.idPregunta, respuesta.idRespuesta)}
                    />
                    {respuesta.respuesta}
                  </label>
                ))
              ) : (
                <p style={{ color: 'gray' }}>No hay respuestas disponibles.</p>
              )}
            </div>
          </div>
        ))}
        <button type="submit">Enviar Test</button>
      </form>
      <button onClick={volverAlHome} style={{ marginTop: '20px' }}>Volver al Home</button>
    </div>
  );
}

export default TestCoche;
