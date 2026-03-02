import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate, useParams } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import '../css/TestCoche.css';
import '../css/Global.css';

function TestCoche() {
  const [preguntas, setPreguntas] = useState([]);
  const [respuestasSeleccionadas, setRespuestasSeleccionadas] = useState({});
  const [corregido, setCorregido] = useState(false);
  const [, setAciertos] = useState(0);
  const navigate = useNavigate();
  const { idExamen } = useParams();

  useEffect(() => {
    const token = localStorage.getItem('token');
    axios.get(`http://localhost:8080/api/preguntas/examen/${idExamen}`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })
    .then(res => setPreguntas(res.data))
    .catch(err => console.error('Error al cargar preguntas:', err));
  }, [idExamen]);

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

    // Mostrar resultado como alerta
    if (contador >= 27) {
      alert(`¡Aprobado! Has acertado ${contador} de 30 preguntas.`);
    } else {
      alert(`Suspenso. Has acertado ${contador} de 30 preguntas.`);
    }

    const token = localStorage.getItem('token');
    const idUsuario = localStorage.getItem('idUsuario');

    try {
      await axios.post('http://localhost:8080/api/usuarios-examenes/guardar-resultado', {
        idUsuario: idUsuario,
        idExamen: parseInt(idExamen),
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
    <>
      <Header />
      <div className="test-container">
        <h2>Test Teórico</h2>

        <form onSubmit={handleSubmit}>
          <div className="test-grid">
            {preguntas.map((pregunta, index) => (
              <div key={pregunta.idPregunta} className="test-question">
                <strong>{index + 1}. {pregunta.enunciado}</strong>
                <div className="test-options">
                  {pregunta.respuestas.map(respuesta => {
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
                      <label key={respuesta.idRespuesta} className="test-option-label" style={estilo}>
                        <span>{respuesta.respuesta}</span>
                        <input
                          type="radio"
                          name={`pregunta-${pregunta.idPregunta}`}
                          value={respuesta.idRespuesta}
                          checked={seleccionada}
                          onChange={() => handleSeleccionRespuesta(pregunta.idPregunta, respuesta.idRespuesta)}
                          disabled={corregido}
                        />
                      </label>
                    );
                  })}
                </div>
              </div>
            ))}
          </div>

          <div className="test-button-grid">
            {!corregido ? (
              <button type="submit">Enviar Test</button>
            ) : (
              <>
                <button type="button" onClick={reiniciarTest}>Reintentar</button>
                <button type="button" onClick={volverAlHome}>Volver al Home</button>
              </>
            )}
          </div>
        </form>
      </div>
      <Footer />
    </>
  );
}

export default TestCoche;
