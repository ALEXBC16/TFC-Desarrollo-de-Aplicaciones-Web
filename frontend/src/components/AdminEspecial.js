import React, { useEffect, useState } from 'react';
import axios from 'axios';
import Header from '../components/Header';
import Footer from '../components/Footer';
import '../css/AdminEspecial.css';

const AdminEspecial = () => {
  const [examenes, setExamenes] = useState([]);
  const [pregunta, setPregunta] = useState('');
  const [examenId, setExamenId] = useState('');
  const [mensaje, setMensaje] = useState('');
  const [preguntasExamen, setPreguntasExamen] = useState([]);
  const [examenActivo, setExamenActivo] = useState(null);

  useEffect(() => {
    const fetchExamenes = async () => {
      try {
        const token = localStorage.getItem('token');
        const response = await axios.get('http://localhost:8080/api/examenes', {
          headers: { Authorization: `Bearer ${token}` },
        });
        setExamenes(response.data);
      } catch (error) {
        console.error("Error al cargar exámenes", error);
        setMensaje("No tienes permisos para ver los exámenes.");
      }
    };

    fetchExamenes();
  }, []);

  const handleAddPregunta = async (e) => {
    e.preventDefault();
    try {
      const token = localStorage.getItem('token');
      await axios.post(
        'http://localhost:8080/api/preguntas/crear',
        { enunciado: pregunta, examenId: examenId },
        { headers: { Authorization: `Bearer ${token}` } }
      );
      setMensaje('Pregunta añadida con éxito.');
      setPregunta('');
      setExamenId('');
    } catch (err) {
      console.error(err);
      setMensaje('Error al añadir la pregunta.');
    }
  };

  const handleVerPreguntas = async (id) => {
    try {
      const token = localStorage.getItem('token');
      const response = await axios.get(`http://localhost:8080/api/preguntas/examen/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      setPreguntasExamen(response.data);
      setExamenActivo(id);
    } catch (err) {
      console.error('Error al cargar preguntas', err);
      setPreguntasExamen([]);
      setMensaje('No se pudieron cargar las preguntas.');
    }
  };

  const niveles = ['Iniciación', 'Medio', 'Avanzado'];

  return (
    <>
      <Header />
      <div className="admin-especial-container">
        <h1>Panel de Administración Especial</h1>

        <section className="lista-examenes">
          <h2>Exámenes disponibles</h2>
          {niveles.map((nivel) => (
            <div key={nivel}>
              <h3>{nivel}</h3>
              <div className="botonera-examenes">
                {examenes
                  .filter((ex) => ex.nivel === nivel)
                  .map((examen) => (
                    <button key={examen.idExamen} onClick={() => handleVerPreguntas(examen.idExamen)}>
                      {examen.nombre}
                    </button>
                  ))}
              </div>
            </div>
          ))}

          {examenActivo && (
            <div className="preguntas-examen">
              <h3>Preguntas del examen</h3>
              <ul>
                {preguntasExamen.map((p, index) => (
                  <li key={index}>{p.enunciado}</li>
                ))}
              </ul>
            </div>
          )}
        </section>

        <section className="formulario-pregunta">
          <h2>Añadir Pregunta</h2>
          <form onSubmit={handleAddPregunta}>
            <input
              type="text"
              placeholder="Enunciado de la pregunta"
              value={pregunta}
              onChange={(e) => setPregunta(e.target.value)}
              required
            />
            <select value={examenId} onChange={(e) => setExamenId(e.target.value)} required>
              <option value="">Selecciona un examen</option>
              {examenes.map((examen) => (
                <option key={examen.idExamen} value={examen.idExamen}>
                  {examen.nombre} - {examen.nivel}
                </option>
              ))}
            </select>
            <button type="submit">Añadir</button>
          </form>
          {mensaje && <p>{mensaje}</p>}
        </section>
      </div>
      <Footer />
    </>
  );
};

export default AdminEspecial;
