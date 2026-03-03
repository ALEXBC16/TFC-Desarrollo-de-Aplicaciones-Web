package edu.lopezalejandro._aMarcha.config;

import edu.lopezalejandro._aMarcha.security.JwtAuthenticationFilter;
import edu.lopezalejandro._aMarcha.security.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwtUtil) throws Exception {

        http
            // =========================
            // CORS
            // =========================
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of(
                        "http://localhost:3000",
                        "http://localhost:5173",
                        "https://tfc-desarrollo-de-aplicaciones-web.vercel.app"
                ));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                config.setAllowCredentials(true);
                return config;
            }))

            // =========================
            // CSRF DESACTIVADO (API REST)
            // =========================
            .csrf(csrf -> csrf.disable())

            // =========================
            // AUTORIZACIÓN
            // =========================
            .authorizeHttpRequests(auth -> auth

                // --- RUTAS PÚBLICAS ---
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/crear-con-pago").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/paypal/create-order").permitAll()

                // Recursos públicos no API
                .requestMatchers("/", "/manifest.json", "/favicon.ico", "/error").permitAll()

                // --- RUTAS CON ROLES ---
                .requestMatchers("/api/preguntas/examen-con-respuestas/**")
                    .hasAuthority("ROLE_ADMIN_ESPECIAL")

                .requestMatchers("/api/preguntas/**")
                    .hasAnyRole("SUPERUSUARIO", "USUARIO_COCHES", "USUARIO_MOTOS", "ADMIN_ESPECIAL")

                .requestMatchers("/api/respuestas/**")
                    .hasAnyRole("SUPERUSUARIO", "USUARIO_COCHES", "USUARIO_MOTOS")

                .requestMatchers(HttpMethod.POST, "/api/usuarios-examenes/guardar-resultado")
                    .hasAnyAuthority("ROLE_USUARIO_COCHES", "ROLE_USUARIO_MOTOS",
                                     "ROLE_SUPERUSUARIO", "ROLE_ADMIN_ESPECIAL")

                .requestMatchers("/api/usuarios/**").authenticated()

                // --- TODO LO QUE SEA /api/** REQUIERE AUTENTICACIÓN ---
                .requestMatchers("/api/**").authenticated()

                // --- TODO LO DEMÁS PERMITIDO ---
                .anyRequest().permitAll()
            )

            // =========================
            // JWT FILTER
            // =========================
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil),
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // =========================
    // USERS DE PRUEBA (OPCIONAL)
    // =========================
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

    // =========================
    // PASSWORD ENCODER
    // =========================
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}