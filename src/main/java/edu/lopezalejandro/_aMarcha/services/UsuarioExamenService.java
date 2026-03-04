package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.UsuarioExamen;

import java.util.List;
import java.util.Optional;

public interface UsuarioExamenService {

    List<UsuarioExamen> findAll();

    Optional<UsuarioExamen> findById(int id);

    UsuarioExamen save(UsuarioExamen usuarioExamen);

    void deleteById(int id);

    List<UsuarioExamen> findByUsuarioId(int idUsuario);

    List<UsuarioExamen> findByExamenId(int idExamen);

    List<UsuarioExamen> findTop10ByUsuarioIdOrderByFechaRealizacionDesc(int idUsuario);
}
