package edu.lopezalejandro._aMarcha.repositories;

import edu.lopezalejandro._aMarcha.entities.UsuarioExamen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioExamenRepository extends JpaRepository<UsuarioExamen, Integer> {
    List<UsuarioExamen> findByUsuario_IdUsuario(int idUsuario);
    List<UsuarioExamen> findByExamen_IdExamen(int idExamen);
}
