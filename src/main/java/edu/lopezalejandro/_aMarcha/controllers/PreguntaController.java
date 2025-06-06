package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.entities.Pregunta;
import edu.lopezalejandro._aMarcha.services.PreguntaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/preguntas")
@CrossOrigin(origins = "*")
public class PreguntaController {

    @Autowired
    private PreguntaService preguntaService;

    @GetMapping
    public List<Pregunta> getAll() {
        return preguntaService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Pregunta> getById(@PathVariable int id) {
        return preguntaService.findById(id);
    }

    @GetMapping("/examen/{idExamen}")
    public List<Pregunta> getByExamen(@PathVariable int idExamen) {
        return preguntaService.findByExamenId(idExamen);
    }

    @PostMapping
    public Pregunta create(@RequestBody Pregunta pregunta) {
        return preguntaService.save(pregunta);
    }

    @PutMapping("/{id}")
    public Pregunta update(@PathVariable int id, @RequestBody Pregunta pregunta) {
        pregunta.setIdPregunta(id);
        return preguntaService.save(pregunta);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        preguntaService.deleteById(id);
    }
}
