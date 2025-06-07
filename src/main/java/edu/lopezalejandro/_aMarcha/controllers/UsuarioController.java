package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.entities.Usuario;
import edu.lopezalejandro._aMarcha.services.PayPalService;
import edu.lopezalejandro._aMarcha.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public Usuario create(@RequestBody Usuario usuario) {
        String encriptada = passwordEncoder.encode(usuario.getContrasenaUsuario());
        usuario.setContrasenaUsuario(encriptada);
        return usuarioService.save(usuario);
    }

    @PostMapping("/crear-con-pago")
    public ResponseEntity<?> crearConPago(@RequestBody Map<String, Object> datos) {
        String orderId = (String) datos.get("orderId");
        String nombreUsuario = (String) datos.get("nombreUsuario");
        String contrasenaUsuario = (String) datos.get("contrasenaUsuario");
        String fotoPerfil = (String) datos.get("fotoPerfil");
        int tipoSuscripcion = (int) datos.get("tipoSuscripcion");

        if (!payPalService.verificarPago(orderId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pago no verificado.");
        }

        if (usuarioService.findByNombreUsuario(nombreUsuario) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nombre de usuario ya en uso.");
        }

        Usuario u = new Usuario();
        u.setNombreUsuario(nombreUsuario);
        u.setContrasenaUsuario(passwordEncoder.encode(contrasenaUsuario));
        u.setFotoPerfil(fotoPerfil);
        u.setTipoSuscripcion(tipoSuscripcion);

        Usuario guardado = usuarioService.save(u);
        return ResponseEntity.ok(guardado);
    }

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
