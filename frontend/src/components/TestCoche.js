import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import { jwtDecode } from 'jwt-decode';

function TestCoche() {
  const [preguntas, setPreguntas] = useState([]);
  const [respuestasSeleccionadas, setRespuestasSeleccionadas] = useState({});
  const [corregido, setCorregido] = useState(false);
  const [aciertos, setAciertos] = useState(0);
  const navigate = useNavigate();
  const idExamen = 1;

  useEffect(() => {
    const token = localStorage.getItem('token');
    axios.get(`http://localhost:8080/api/preguntas/examen/${idExamen}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(res => setPreguntas(res.data))
    .catch(err => console.error('Error al cargar preguntas:', err));
  }, []);

  const handleSeleccionRespuesta = (idPregunta, idRespuesta) => {
    if (!corregido) {
      setRespuestasSeleccionadas(prev => ({
        ...prev,
        [idPregunta]: idRespuesta
      }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (corregido) return;

    let contador = 0;
    preguntas.forEach(pregunta => {
      const seleccion = respuestasSeleccionadas[pregunta.idPregunta];
      const correcta = pregunta.respuestas.find(r => r.esCorrecta);
      if (correcta && seleccion === correcta.idRespuesta) {
        contador++;
      }
    });

    setAciertos(contador);
    setCorregido(true);

    const token = localStorage.getItem('token');
    const idUsuario = localStorage.getItem('idUsuario'); // ✅ ID desde localStorage

    try {
      await axios.post('http://localhost:8080/api/usuarios-examenes/guardar-resultado', {
        idUsuario: idUsuario,
        idExamen: idExamen,
        nota: contador
      }, {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      console.log("Resultado guardado correctamente.");
    } catch (error) {
      console.error("Error al guardar el resultado:", error);
    }
  };

  const volverAlHome = () => {
    navigate('/home');
  };

  const reiniciarTest = () => {
    setRespuestasSeleccionadas({});
    setCorregido(false);
    setAciertos(0);
  };

  const esRespuestaCorrecta = (pregunta, respuesta) => respuesta.esCorrecta;

  return (
    <div>
      <h2>Test Teórico de Coche</h2>
      {corregido && (
        <div style={{ marginBottom: '20px' }}>
          <h3 style={{ color: aciertos >= 27 ? 'green' : 'red' }}>
            {aciertos >= 27 ? '¡Aprobado!' : 'Suspenso'}
          </h3>
          <p>Has acertado {aciertos} de 30 preguntas.</p>
        </div>
      )}
      <form onSubmit={handleSubmit}>
        {preguntas.map(pregunta => (
          <div key={pregunta.idPregunta} style={{ marginBottom: '1rem' }}>
            <strong>{pregunta.enunciado}</strong>
            <div>
              {Array.isArray(pregunta.respuestas) && pregunta.respuestas.map(respuesta => {
                const seleccionada = respuestasSeleccionadas[pregunta.idPregunta] === respuesta.idRespuesta;
                const correcta = esRespuestaCorrecta(pregunta, respuesta);
                let estilo = {};

                if (corregido) {
                  estilo = {
                    backgroundColor: correcta
                      ? 'lightgreen'
                      : (seleccionada ? 'salmon' : 'transparent')
                  };
                }

                return (
                  <label key={respuesta.idRespuesta} style={{ display: 'block', ...estilo }}>
                    <input
                      type="radio"
                      name={`pregunta-${pregunta.idPregunta}`}
                      value={respuesta.idRespuesta}
                      checked={seleccionada}
                      onChange={() => handleSeleccionRespuesta(pregunta.idPregunta, respuesta.idRespuesta)}
                      disabled={corregido}
                    />
                    {respuesta.respuesta}
                  </label>
                );
              })}
            </div>
          </div>
        ))}
        {!corregido ? (
          <button type="submit">Enviar Test</button>
        ) : (
          <>
            <button type="button" onClick={reiniciarTest}>Reintentar</button>
            <button type="button" onClick={volverAlHome} style={{ marginLeft: '10px' }}>Volver al Home</button>
          </>
        )}
      </form>
    </div>
  );
}

export default TestCoche;
