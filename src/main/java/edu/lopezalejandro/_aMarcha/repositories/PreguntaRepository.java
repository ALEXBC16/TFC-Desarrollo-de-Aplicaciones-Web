package edu.lopezalejandro._aMarcha.repositories;

import edu.lopezalejandro._aMarcha.entities.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {

    List<Pregunta> findByExamen_IdExamen(int idExamen);

    List<Pregunta> findByCategoria(Integer categoria);

    @Query(value = """
        SELECT *
        FROM preguntas p
        WHERE 
            (:tipo = 0 OR p.categoria = :tipo)
        ORDER BY RANDOM()
        LIMIT :cantidad
        """, nativeQuery = true)
    List<Pregunta> findPreguntasAleatoriasPorTipo(
            @Param("cantidad") int cantidad,
            @Param("tipo") int tipo
    );
}