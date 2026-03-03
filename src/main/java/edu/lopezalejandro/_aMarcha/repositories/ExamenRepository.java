package edu.lopezalejandro._aMarcha.repositories;

import edu.lopezalejandro._aMarcha.entities.Examen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamenRepository extends JpaRepository<Examen, Integer> {

    Examen findByNombre(String nombre);

    List<Examen> findByCategoria(Integer categoria);
}