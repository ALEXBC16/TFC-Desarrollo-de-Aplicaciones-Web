package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.entities.Usuario;
import edu.lopezalejandro._aMarcha.services.EmailService;
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
    
    @Autowired
    private EmailService emailService;


    @PostMapping
    public ResponseEntity<?> create(@RequestBody Usuario usuario) {
        // Validación mínima
        if (usuario.getNombreUsuario() == null || usuario.getCorreoElectronico() == null || usuario.getContrasenaUsuario() == null) {
            return ResponseEntity.badRequest().body("Todos los campos son obligatorios.");
        }

        if (usuarioService.existsByNombreUsuario(usuario.getNombreUsuario())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Nombre de usuario ya en uso.");
        }

        if (usuarioService.existsByCorreoElectronico(usuario.getCorreoElectronico())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo electrónico ya registrado.");
        }

        usuario.setContrasenaUsuario(passwordEncoder.encode(usuario.getContrasenaUsuario()));
        return ResponseEntity.ok(usuarioService.save(usuario));
    }

    @PostMapping("/crear-con-pago")
        public ResponseEntity<?> crearConPago(@RequestBody Map<String, Object> datos) {
            try {

                String orderId = datos.get("orderId") != null ? datos.get("orderId").toString() : null;
                String nombreUsuario = datos.get("nombreUsuario") != null ? datos.get("nombreUsuario").toString() : null;
                String contrasenaUsuario = datos.get("contrasenaUsuario") != null ? datos.get("contrasenaUsuario").toString() : null;
                String correoElectronico = datos.get("correoElectronico") != null ? datos.get("correoElectronico").toString() : null;
                String fotoPerfil = datos.get("fotoPerfil") != null ? datos.get("fotoPerfil").toString() : "";

                int tipoSuscripcion = Integer.parseInt(datos.get("tipoSuscripcion").toString());

                if (orderId == null) {
                    return ResponseEntity.badRequest().body("OrderId no recibido.");
                }

                if (!payPalService.verificarPago(orderId)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pago no verificado.");
                }

                if (nombreUsuario == null || contrasenaUsuario == null || correoElectronico == null) {
                    return ResponseEntity.badRequest().body("Todos los campos son obligatorios.");
                }

                if (usuarioService.existsByNombreUsuario(nombreUsuario)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Nombre de usuario ya en uso.");
                }

                if (usuarioService.existsByCorreoElectronico(correoElectronico)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Correo electrónico ya registrado.");
                }

                Usuario u = new Usuario();
                u.setNombreUsuario(nombreUsuario);
                u.setContrasenaUsuario(passwordEncoder.encode(contrasenaUsuario));
                u.setCorreoElectronico(correoElectronico);
                u.setFotoPerfil(fotoPerfil);
                u.setTipoSuscripcion(tipoSuscripcion);

                Usuario guardado = usuarioService.save(u);

                try {
                    emailService.enviarCorreoConfirmacion(correoElectronico, nombreUsuario);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return ResponseEntity.ok(guardado);

            } catch (Exception e) {

                e.printStackTrace();

                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error en el servidor: " + e.getMessage());
            }
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
