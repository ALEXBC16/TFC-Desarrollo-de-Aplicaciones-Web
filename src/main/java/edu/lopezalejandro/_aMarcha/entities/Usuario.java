package edu.lopezalejandro._aMarcha.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUsuario;

    @Column(name = "Nombre_Usuario", unique = true, nullable = false)
    private String nombreUsuario;

    @Column(name = "Contraseña_Usuario", nullable = false)
    private String contrasenaUsuario;

    @Column(name = "Foto_perfil")
    private String fotoPerfil;

    @Column(name = "Tipo_Suscripcion", nullable = false)
    private int tipoSuscripcion;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<UsuarioExamen> examenesRealizados;

    // Getters y Setters
    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getContrasenaUsuario() {
        return contrasenaUsuario;
    }

    public void setContrasenaUsuario(String contrasenaUsuario) {
        this.contrasenaUsuario = contrasenaUsuario;
    }

    public String getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(String fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    public int getTipoSuscripcion() {
        return tipoSuscripcion;
    }

    public void setTipoSuscripcion(int tipoSuscripcion) {
        this.tipoSuscripcion = tipoSuscripcion;
    }

    public List<UsuarioExamen> getExamenesRealizados() {
        return examenesRealizados;
    }

    public void setExamenesRealizados(List<UsuarioExamen> examenesRealizados) {
        this.examenesRealizados = examenesRealizados;
    }
}
