package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Respuesta;

import java.util.List;
import java.util.Optional;

public interface RespuestaService {
    List<Respuesta> findAll();
    Optional<Respuesta> findById(int id);
    Respuesta save(Respuesta respuesta);
    void deleteById(int id);
}
