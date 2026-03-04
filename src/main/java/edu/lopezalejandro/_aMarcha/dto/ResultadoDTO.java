package edu.lopezalejandro._aMarcha.dto;

public class ResultadoDTO {
    private Integer idUsuario;
    private Integer idExamen;
    private Integer nota;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdExamen() {
        return idExamen;
    }

    public void setIdExamen(Integer idExamen) {
        this.idExamen = idExamen;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
