package edu.lopezalejandro._aMarcha.repositories;

import edu.lopezalejandro._aMarcha.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    // Buscar por nombre de usuario 
    Usuario findByNombreUsuario(String nombreUsuario);

    // Verifica si ya existe un nombre de usuario
    boolean existsByNombreUsuario(String nombreUsuario);

    // Verifica si ya existe un correo electrónico
    boolean existsByCorreoElectronico(String correoElectronico);
}
