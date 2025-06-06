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
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                config.setAllowedOrigins(List.of("http://localhost:3000"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                config.setAllowedHeaders(List.of("*"));
                return config;
            }))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/usuarios/nombre/**").permitAll()
                .requestMatchers("/api/preguntas/**").hasAnyRole("SUPERUSUARIO", "USUARIO_COCHES", "USUARIO_MOTOS")
                .requestMatchers("/api/respuestas/**").hasAnyRole("SUPERUSUARIO", "USUARIO_COCHES", "USUARIO_MOTOS")
                .requestMatchers(HttpMethod.POST, "/api/usuarios-examenes/guardar-resultado")
                    .hasAnyAuthority("ROLE_USUARIO_COCHES", "ROLE_USUARIO_MOTOS", "ROLE_SUPERUSUARIO")
                .requestMatchers("/api/usuarios/**").authenticated()
                .anyRequest().authenticated()
            )
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
