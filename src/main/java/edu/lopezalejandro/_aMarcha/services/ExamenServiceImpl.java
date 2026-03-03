package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Examen;
import edu.lopezalejandro._aMarcha.entities.Usuario;
import edu.lopezalejandro._aMarcha.repositories.ExamenRepository;
import edu.lopezalejandro._aMarcha.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ExamenServiceImpl implements ExamenService {

    private static final int SUPERUSUARIO = 0;
    private static final int COCHE = 1;
    private static final int MOTO = 2;
    private static final int ADMIN = 4;

    @Autowired
    private ExamenRepository examenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Examen> findAll() {
        return examenRepository.findAll();
    }

    @Override
    public Optional<Examen> findById(int id) {
        return examenRepository.findById(id);
    }

    @Override
    public Examen save(Examen examen) {
        return examenRepository.save(examen);
    }

    @Override
    public void deleteById(int id) {
        examenRepository.deleteById(id);
    }

    @Override
    public List<Examen> obtenerExamenesPorUsuario(String email) {

        Usuario usuario = usuarioRepository.findByCorreoElectronico(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Integer categoriaUsuario = usuario.getTipoSuscripcion();

        if (categoriaUsuario == SUPERUSUARIO || categoriaUsuario == ADMIN) {
            return examenRepository.findAll();
        }

        return examenRepository.findByCategoria(categoriaUsuario);
    }
}