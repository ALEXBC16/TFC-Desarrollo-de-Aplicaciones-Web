package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Pregunta;

import java.util.List;
import java.util.Optional;

public interface PreguntaService {
    List<Pregunta> findAll();
    Optional<Pregunta> findById(int id);
    Pregunta save(Pregunta pregunta);
    void deleteById(int id);

    // Nuevo método para buscar preguntas por examen
    List<Pregunta> findByExamenId(int idExamen);
}
