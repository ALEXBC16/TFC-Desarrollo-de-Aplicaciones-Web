package edu.lopezalejandro._aMarcha.repositories;

import edu.lopezalejandro._aMarcha.entities.Pregunta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {

    // Preguntas por examen
    List<Pregunta> findByExamen_IdExamen(int idExamen);

    // Preguntas aleatorias (PostgreSQL)
    @Query(value = """
    SELECT p.* 
    FROM preguntas p
    JOIN examenes e ON p.idexamen = e.idexamen
    WHERE 
        (:tipo = 1 AND LOWER(e.nombre) LIKE '%coche%')
        OR
        (:tipo = 2 AND LOWER(e.nombre) LIKE '%moto%')
        OR
        (:tipo = 0)
    ORDER BY RANDOM()
    LIMIT :cantidad
    """, nativeQuery = true)
    List<Pregunta> findPreguntasAleatoriasPorTipo(
        @Param("cantidad") int cantidad,
        @Param("tipo") int tipo
);
}