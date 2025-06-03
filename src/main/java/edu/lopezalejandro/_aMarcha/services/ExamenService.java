package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Examen;

import java.util.List;
import java.util.Optional;

public interface ExamenService {
    List<Examen> findAll();
    Optional<Examen> findById(int id);
    Examen save(Examen examen);
    void deleteById(int id);
}
