package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.entities.Usuario;
import edu.lopezalejandro._aMarcha.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAll() {
        return usuarioService.findAll();
    }

    @GetMapping("/{id}")
    public Usuario getById(@PathVariable int id) {
        return usuarioService.findById(id);
    }

    @GetMapping("/nombre/{nombre}")
    public Usuario getByNombre(@PathVariable String nombre) {
        return usuarioService.findByNombreUsuario(nombre);
    }

    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) {
        return usuarioService.save(usuario);
    }

    @PutMapping("/{id}")
    public Usuario update(@PathVariable int id, @RequestBody Usuario usuario) {
        usuario.setIdUsuario(id);
        return usuarioService.save(usuario);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        usuarioService.deleteById(id);
    }
}
