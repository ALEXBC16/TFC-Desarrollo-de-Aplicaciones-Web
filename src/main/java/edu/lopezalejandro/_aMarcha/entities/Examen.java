package edu.lopezalejandro._aMarcha.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Examenes")
public class Examen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idExamenes;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Nivel nivel;

    @OneToMany(mappedBy = "examen", cascade = CascadeType.ALL)
    private List<Pregunta> preguntas;

    public enum Nivel {
        Iniciacion, Medio, Avanzado
    }

    // Getters y Setters
    public int getIdExamenes() {
        return idExamenes;
    }

    public void setIdExamenes(int idExamenes) {
        this.idExamenes = idExamenes;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
