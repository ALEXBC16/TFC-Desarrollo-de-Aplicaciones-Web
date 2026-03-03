package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.entities.Examen;
import edu.lopezalejandro._aMarcha.services.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/examenes")
@CrossOrigin(origins = "*")
public class ExamenController {

    @Autowired
    private ExamenService examenService;

    @GetMapping
    public List<Examen> getAll(Authentication authentication) {
        String email = authentication.getName();
        return examenService.obtenerExamenesPorUsuario(email);
    }

    @GetMapping("/{id}")
    public Optional<Examen> getById(@PathVariable int id) {
        return examenService.findById(id);
    }

    @PostMapping
    public Examen create(@RequestBody Examen examen) {
        return examenService.save(examen);
    }

    @PutMapping("/{id}")
    public Examen update(@PathVariable int id, @RequestBody Examen examen) {
        examen.setIdExamen(id);
        return examenService.save(examen);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        examenService.deleteById(id);
    }
}