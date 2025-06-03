package edu.lopezalejandro._aMarcha.services;

import edu.lopezalejandro._aMarcha.entities.Respuesta;
import edu.lopezalejandro._aMarcha.repositories.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RespuestaServiceImpl implements RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Override
    public List<Respuesta> findAll() {
        return respuestaRepository.findAll();
    }

    @Override
    public Optional<Respuesta> findById(int id) {
        return respuestaRepository.findById(id);
    }

    @Override
    public Respuesta save(Respuesta respuesta) {
        return respuestaRepository.save(respuesta);
    }

    @Override
    public void deleteById(int id) {
        respuestaRepository.deleteById(id);
    }
}
