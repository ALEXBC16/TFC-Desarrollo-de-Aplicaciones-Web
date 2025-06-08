package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.dto.ResultadoDTO;
import edu.lopezalejandro._aMarcha.entities.Examen;
import edu.lopezalejandro._aMarcha.entities.Usuario;
import edu.lopezalejandro._aMarcha.entities.UsuarioExamen;
import edu.lopezalejandro._aMarcha.repositories.ExamenRepository;
import edu.lopezalejandro._aMarcha.repositories.UsuarioRepository;
import edu.lopezalejandro._aMarcha.services.UsuarioExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios-examenes")
@CrossOrigin(origins = "*")
public class UsuarioExamenController {

    @Autowired
    private UsuarioExamenService usuarioExamenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ExamenRepository examenRepository;

    @GetMapping
    public List<UsuarioExamen> getAll() {
        return usuarioExamenService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<UsuarioExamen> getById(@PathVariable int id) {
        return usuarioExamenService.findById(id);
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<UsuarioExamen> getByUsuario(@PathVariable int idUsuario) {
        return usuarioExamenService.findByUsuarioId(idUsuario);
    }

    @GetMapping("/examen/{idExamen}")
    public List<UsuarioExamen> getByExamen(@PathVariable int idExamen) {
        return usuarioExamenService.findByExamenId(idExamen);
    }

    @PostMapping
    public UsuarioExamen create(@RequestBody UsuarioExamen usuarioExamen) {
        return usuarioExamenService.save(usuarioExamen);
    }

    @PutMapping("/{id}")
    public UsuarioExamen update(@PathVariable int id, @RequestBody UsuarioExamen usuarioExamen) {
        usuarioExamen.setId(id);
        return usuarioExamenService.save(usuarioExamen);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        usuarioExamenService.deleteById(id);
    }

    @PostMapping("/guardar-resultado")
    public UsuarioExamen guardarResultado(@RequestBody ResultadoDTO resultado) {
        Usuario usuario = usuarioRepository.findById(resultado.getIdUsuario()).orElseThrow();
        Examen examen = examenRepository.findById(resultado.getIdExamen()).orElseThrow();

        UsuarioExamen ue = new UsuarioExamen();
        ue.setUsuario(usuario);
        ue.setExamen(examen);
        ue.setNota(resultado.getNota());
        ue.setFechaRealizacion(LocalDateTime.now());  // Fecha y hora

        return usuarioExamenService.save(ue);
    }

    @GetMapping("/ultimos/{idUsuario}")
    public List<UsuarioExamen> getUltimosExamenes(@PathVariable int idUsuario) {
        return usuarioExamenService.findByUsuarioId(idUsuario).stream()
            .sorted((a, b) -> b.getFechaRealizacion().compareTo(a.getFechaRealizacion()))
            .limit(10)
            .toList();
    }


}
