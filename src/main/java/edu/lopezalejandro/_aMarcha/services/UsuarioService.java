package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {
    List<Usuario> findAll();
    Optional<Usuario> findById(int id);
    Usuario save(Usuario usuario);
    void deleteById(int id);
    Usuario findByNombreUsuario(String nombreUsuario);
}
