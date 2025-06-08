package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Pregunta;
import edu.lopezalejandro._aMarcha.repositories.PreguntaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PreguntaServiceImpl implements PreguntaService {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Override
    public List<Pregunta> findAll() {
        return preguntaRepository.findAll();
    }

    @Override
    public Optional<Pregunta> findById(int id) {
        return preguntaRepository.findById(id);
    }

    @Override
    public Pregunta save(Pregunta pregunta) {
        return preguntaRepository.save(pregunta);
    }

    @Override
    public void deleteById(int id) {
        preguntaRepository.deleteById(id);
    }

    @Override
    public List<Pregunta> findByExamenId(int idExamen) {
        return preguntaRepository.findByExamen_IdExamen(idExamen);
    }

    @Override
    public List<Pregunta> findPreguntasConRespuestasPorExamen(int idExamen) {
        List<Pregunta> preguntas = preguntaRepository.findByExamen_IdExamen(idExamen);
        preguntas.forEach(p -> p.getRespuestas().size()); // Fuerza carga de respuestas si son LAZY
        return preguntas;
    }
}
