package edu.lopezalejandro._aMarcha.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Respuestas")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idRespuesta;

    private String respuesta;

    private boolean esCorrecta;

    @ManyToOne
    @JoinColumn(name = "IdPregunta")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Pregunta pregunta;


    // Getters y Setters
    public int getIdRespuesta() {
        return idRespuesta;
    }

    public void setIdRespuesta(int idRespuesta) {
        this.idRespuesta = idRespuesta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public boolean isEsCorrecta() {
        return esCorrecta;
    }

    public void setEsCorrecta(boolean esCorrecta) {
        this.esCorrecta = esCorrecta;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }
}
