package edu.lopezalejandro._aMarcha.controllers;

import edu.lopezalejandro._aMarcha.entities.Usuario;
import edu.lopezalejandro._aMarcha.repositories.UsuarioRepository;
import edu.lopezalejandro._aMarcha.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String nombreUsuario = body.get("nombreUsuario");
        String contrasena = body.get("contrasenaUsuario");

        Usuario usuario = usuarioRepository.findByNombreUsuario(nombreUsuario);
        if (usuario == null || !passwordEncoder.matches(contrasena, usuario.getContrasenaUsuario())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        String token = jwtUtil.generateToken(nombreUsuario);
        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        response.put("nombreUsuario", usuario.getNombreUsuario());
        return response;
    }
} 
