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
}
