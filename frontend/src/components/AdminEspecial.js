import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import '../css/AdminEspecial.css';

const AdminEspecial = () => {
  const [examenes, setExamenes] = useState([]);
  const [pregunta, setPregunta] = useState('');
  const [categoria, setCategoria] = useState('');
  const [respuestas, setRespuestas] = useState([
    { texto: '', esCorrecta: false },
    { texto: '', esCorrecta: false },
    { texto: '', esCorrecta: false },
    { texto: '', esCorrecta: false },
  ]);
  const [mensaje, setMensaje] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const fetchExamenes = async () => {
      try {
        const token = localStorage.getItem('token');

        const response = await axios.get(
          `${process.env.REACT_APP_API_URL}/api/examenes`,
          {
            headers: { Authorization: `Bearer ${token}` },
          }
        );

        setExamenes(response.data);
      } catch (error) {
        console.error("Error al cargar exámenes", error);
        setMensaje("No tienes permisos para ver los exámenes.");
      }
    };

    fetchExamenes();
  }, []);

  const handleRespuestaChange = (index, field, value) => {
    const nuevasRespuestas = [...respuestas];

    if (field === 'esCorrecta') {
      nuevasRespuestas.forEach((r, i) => (r.esCorrecta = i === index));
    } else {
      nuevasRespuestas[index][field] = value;
    }

    setRespuestas(nuevasRespuestas);
  };

  const handleAddPregunta = async (e) => {
    e.preventDefault();

    try {
      const token = localStorage.getItem('token');

      await axios.post(
        `${process.env.REACT_APP_API_URL}/api/preguntas`,
        {
          enunciado: pregunta,
          categoria: categoria,
          respuestas: respuestas.map(r => ({
            respuesta: r.texto,
            esCorrecta: r.esCorrecta
          }))
        },
        {
          headers: { Authorization: `Bearer ${token}` }
        }
      );

      setMensaje('Pregunta y respuestas añadidas con éxito.');
      setPregunta('');
      setCategoria('');
      setRespuestas([
        { texto: '', esCorrecta: false },
        { texto: '', esCorrecta: false },
        { texto: '', esCorrecta: false },
        { texto: '', esCorrecta: false },
      ]);

    } catch (err) {
      console.error(err);
      setMensaje('Error al añadir la pregunta.');
    }
  };

  const handleLogout = () => {
    localStorage.clear();
    navigate('/');
  };

  const niveles = ['Iniciacion', 'Medio', 'Avanzado'];

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
                    <button
                      key={examen.idExamen}
                      onClick={() =>
                        navigate(`/admin-examen/${examen.idExamen}`)
                      }
                    >
                      {examen.nombre}
                    </button>
                  ))}
              </div>
            </div>
          ))}
        </section>

        <section className="formulario-pregunta">
          <h2>Añadir Pregunta sin examen</h2>

          <form onSubmit={handleAddPregunta}>

            <input
              type="text"
              placeholder="Enunciado de la pregunta"
              value={pregunta}
              onChange={(e) => setPregunta(e.target.value)}
              required
            />

            <select
              value={categoria}
              onChange={(e) => setCategoria(e.target.value)}
              required
            >
              <option value="">Selecciona categoría</option>
              <option value="1">Coche</option>
              <option value="2">Moto</option>
            </select>

            {respuestas.map((resp, i) => (
              <div
                key={i}
                style={{
                  marginBottom: '10px',
                  display: 'flex',
                  alignItems: 'center',
                  gap: '1rem'
                }}
              >
                <input
                  type="text"
                  placeholder={`Respuesta ${i + 1}`}
                  value={resp.texto}
                  onChange={(e) =>
                    handleRespuestaChange(i, 'texto', e.target.value)
                  }
                  required
                />

                <label className="radio-label">
                  <span>Correcta</span>

                  <input
                    type="radio"
                    name="respuestaCorrecta"
                    checked={resp.esCorrecta}
                    onChange={() =>
                      handleRespuestaChange(i, 'esCorrecta', true)
                    }
                  />

                </label>
              </div>
            ))}

            <button type="submit">Añadir</button>

          </form>

          {mensaje && <p>{mensaje}</p>}
        </section>
      </div>

      <div className="logout-wrapper">
        <button className="logout-button" onClick={handleLogout}>
          Cerrar sesión
        </button>
      </div>

      <Footer />
    </>
  );
};

export default AdminEspecial;