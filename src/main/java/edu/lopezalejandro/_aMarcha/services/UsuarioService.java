package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Usuario;

import java.util.List;

public interface UsuarioService {
    List<Usuario> findAll();
    Usuario findById(int id);
    Usuario save(Usuario usuario);
    void deleteById(int id);
    Usuario findByNombreUsuario(String nombreUsuario);
}
