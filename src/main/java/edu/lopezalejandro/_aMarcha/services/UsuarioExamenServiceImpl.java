package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.UsuarioExamen;
import edu.lopezalejandro._aMarcha.repositories.UsuarioExamenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioExamenServiceImpl implements UsuarioExamenService {

    @Autowired
    private UsuarioExamenRepository usuarioExamenRepository;

    @Override
    public List<UsuarioExamen> findAll() {
        return usuarioExamenRepository.findAll();
    }

    @Override
    public Optional<UsuarioExamen> findById(int id) {
        return usuarioExamenRepository.findById(id);
    }

    @Override
    public UsuarioExamen save(UsuarioExamen usuarioExamen) {
        return usuarioExamenRepository.save(usuarioExamen);
    }

    @Override
    public void deleteById(int id) {
        usuarioExamenRepository.deleteById(id);
    }

    @Override
    public List<UsuarioExamen> findByUsuarioId(int idUsuario) {
        return usuarioExamenRepository.findByUsuario_IdUsuario(idUsuario);
    }

    @Override
    public List<UsuarioExamen> findByExamenId(int idExamen) {
        return usuarioExamenRepository.findByExamen_IdExamen(idExamen);
    }

    @Override
    public List<UsuarioExamen> findTop10ByUsuarioIdOrderByFechaRealizacionDesc(int idUsuario) {
        return usuarioExamenRepository
                .findTop10ByUsuario_IdUsuarioOrderByFechaRealizacionDesc(idUsuario);
}

}
