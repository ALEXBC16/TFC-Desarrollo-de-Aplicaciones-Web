package edu.lopezalejandro._aMarcha.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // Configuración de seguridad HTTP
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF para facilitar pruebas con Postman
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/usuarios/**").hasAnyRole("SUPERUSUARIO", "USUARIO_COCHES", "USUARIO_MOTOS")
                .requestMatchers("/api/preguntas/**", "/api/respuestas/**").hasRole("SUPERUSUARIO")
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    // Autenticación en memoria (ejemplo)
    @Bean
    public UserDetailsService users() {
        UserDetails superUser = User.builder()
            .username("admin")
            .password(passwordEncoder().encode("admin123"))
            .roles("SUPERUSUARIO")
            .build();

        UserDetails cocheUser = User.builder()
            .username("coche")
            .password(passwordEncoder().encode("coche123"))
            .roles("USUARIO_COCHES")
            .build();

        UserDetails motoUser = User.builder()
            .username("moto")
            .password(passwordEncoder().encode("moto123"))
            .roles("USUARIO_MOTOS")
            .build();

        return new InMemoryUserDetailsManager(superUser, cocheUser, motoUser);
    }

    // Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
