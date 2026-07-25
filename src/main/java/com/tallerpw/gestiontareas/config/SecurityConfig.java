package com.tallerpw.gestiontareas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad.
 *
 * Día 1-8: todo abierto (permitAll), para no bloquear el desarrollo de
 * las vistas antes de tener autenticación real.
 *
 * Día 9: autenticación real — login, registro, BCrypt, roles y
 * autorización por endpoint (permitAll / authenticated / hasRole).
 *
 * Día 10: se completa el cuadro:
 *   - CSRF ya NO está deshabilitado (ver el comentario más abajo).
 *   - Se configura explícitamente el manejo de sesión (creación y
 *     límite de sesiones simultáneas).
 *   - La autorización por ROL a nivel de ruta (/admin/**) se complementa
 *     con autorización a nivel de DATOS en TareaService (HU-07: cada
 *     usuario gestiona solo sus propias tareas, salvo que sea ADMIN).
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/tareas", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            )
            // Día 10: manejo explícito de sesión.
            // IF_REQUIRED (el valor por defecto) crea una sesión HTTP recién
            // cuando hace falta (por ejemplo, al iniciar sesión) — se deja
            // explícito acá solo para que quede visible en el código.
            // maximumSessions(1) fuerza que cada usuario tenga como máximo
            // una sesión activa: si inicia sesión en un segundo navegador,
            // la primera sesión se invalida automáticamente.
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );

        // Día 10: CSRF HABILITADO. Hasta el Día 9 este bloque terminaba con
        // .csrf(csrf -> csrf.disable()); ahora, al no llamar a csrf() en
        // absoluto, Spring Security usa la protección CSRF por defecto
        // (el token viaja asociado a la sesión HTTP — por eso tiene sentido
        // verlo el mismo día que el manejo de sesión). Con Thymeleaf,
        // cualquier <form th:action="..."> ya inserta automáticamente el
        // campo oculto con el token (integración con RequestDataValueProcessor):
        // no hace falta tocar ningún formulario existente.

        return http.build();
    }

}
