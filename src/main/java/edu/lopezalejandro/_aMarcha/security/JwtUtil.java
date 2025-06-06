package edu.lopezalejandro._aMarcha.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "1amarchaSuperSecretoClaveJWT1234567890"; // mínimo 32 caracteres
    private static final long EXPIRATION_TIME = 86400000; // 1 día en milisegundos

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    //Genera un token JWT con el nombre de usuario y el rol como claims.
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role) // Incluye el rol del usuario
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    //Extrae el nombre de usuario del token.
    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //Extrae el rol del usuario desde el token.
    public String extractRole(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    //Valida si el token es correcto y no está expirado.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
