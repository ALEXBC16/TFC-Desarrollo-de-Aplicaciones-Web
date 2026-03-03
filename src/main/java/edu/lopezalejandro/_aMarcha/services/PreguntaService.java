package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Pregunta;

import java.util.List;
import java.util.Optional;

public interface PreguntaService {

    List<Pregunta> findAll();

    Optional<Pregunta> findById(int id);

    Pregunta save(Pregunta pregunta);

    void deleteById(int id);

    List<Pregunta> findByExamenId(int idExamen);

    List<Pregunta> findPreguntasConRespuestasPorExamen(int idExamen);

    List<Pregunta> findPreguntasAleatoriasPorTipo(int cantidad, int tipo);
}