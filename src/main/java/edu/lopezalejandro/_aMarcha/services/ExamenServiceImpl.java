package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Examen;
import edu.lopezalejandro._aMarcha.repositories.ExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamenServiceImpl implements ExamenService {

    @Autowired
    private ExamenRepository examenRepository;

    @Override
    public List<Examen> findAll() {
        return examenRepository.findAll();
    }

    @Override
    public Optional<Examen> findById(int id) {
        return examenRepository.findById(id);
    }

    @Override
    public Examen save(Examen examen) {
        return examenRepository.save(examen);
    }

    @Override
    public void deleteById(int id) {
        examenRepository.deleteById(id);
    }
}
