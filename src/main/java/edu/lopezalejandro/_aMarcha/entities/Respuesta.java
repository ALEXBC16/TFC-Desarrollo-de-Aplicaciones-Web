package edu.lopezalejandro._aMarcha.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Respuestas")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRepuesta;

    @Column(nullable = false)
    private String respuesta;

    @ManyToOne
    @JoinColumn(name = "IdPregunta")
    private Pregunta pregunta;

    // Getters y Setters
    public int getIdRepuesta() {
        return idRepuesta;
    }

    public void setIdRepuesta(int idRepuesta) {
        this.idRepuesta = idRepuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
}
