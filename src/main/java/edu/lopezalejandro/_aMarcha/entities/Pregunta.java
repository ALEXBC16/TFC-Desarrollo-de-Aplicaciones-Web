package edu.lopezalejandro._aMarcha.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Preguntas")
public class Pregunta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPregunta;

    @Column(name = "Enunciado_Pregunta")
    private String enunciado;

    @ManyToOne
    @JoinColumn(name = "IdExamen")
    private Examen examen;

    @OneToMany(mappedBy = "pregunta", cascade = CascadeType.ALL)
    private List<Respuesta> respuestas;

    // Getters y Setters
    public int getIdPregunta() {
        return idPregunta;
    }

    public void setIdPregunta(int idPregunta) {
        this.idPregunta = idPregunta;
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public Examen getExamen() {
        return examen;
    }

    public void setExamen(Examen examen) {
        this.examen = examen;
    }

    public List<Respuesta> getRespuestas() {
        return respuestas;
    }

    public void setRespuestas(List<Respuesta> respuestas) {
        this.respuestas = respuestas;
    }
}
