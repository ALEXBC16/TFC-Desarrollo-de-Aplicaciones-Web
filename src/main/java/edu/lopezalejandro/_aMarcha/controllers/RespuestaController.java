package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.entities.Respuesta;
import edu.lopezalejandro._aMarcha.services.RespuestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/respuestas")
@CrossOrigin(origins = "*")
public class RespuestaController {

    @Autowired
    private RespuestaService respuestaService;

    @GetMapping
    public List<Respuesta> getAll() {
        return respuestaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Respuesta> getById(@PathVariable int id) {
        return respuestaService.findById(id);
    }

    @GetMapping("/pregunta/{idPregunta}")
    public List<Respuesta> getByPregunta(@PathVariable int idPregunta) {
        return respuestaService.findByPreguntaId(idPregunta);
    }

    @PostMapping
    public Respuesta create(@RequestBody Respuesta respuesta) {
        return respuestaService.save(respuesta);
    }

    @PutMapping("/{id}")
    public Respuesta update(@PathVariable int id, @RequestBody Respuesta respuesta) {
        respuesta.setIdRespuesta(id);
        return respuestaService.save(respuesta);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        respuestaService.deleteById(id);
    }
}
