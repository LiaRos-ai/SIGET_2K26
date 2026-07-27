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
 *
 * Día 11: se agrega /api/** a la lista de rutas públicas (permitAll) Y
 * se la exime de CSRF. Esto es una decisión TEMPORAL y a propósito, no
 * un descuido: la API REST recién nace hoy, y protegerla correctamente
 * (con su propio mecanismo, no con sesión de navegador) es justamente
 * el contenido del Día 12 ("Seguridad transaccional"). Mientras tanto,
 * cualquiera puede probar la API libremente desde Postman sin necesidad
 * de loguearse primero.
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
                .requestMatchers("/api/**").permitAll() // Día 11: abierta a propósito — se protege el Día 12
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
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            // Día 11: /api/** queda exenta de CSRF. El token CSRF viaja
            // asociado a la sesión de un navegador con cookies; un cliente
            // de API típico (Postman, una app externa) no tiene ese
            // contexto. Las APIs REST convencionalmente se protegen con
            // otro mecanismo (API keys, JWT — Día 12), no con CSRF.
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        // Día 10: CSRF sigue HABILITADO para el resto de la aplicación
        // (las vistas Thymeleaf). Con Thymeleaf, cualquier
        // <form th:action="..."> ya inserta automáticamente el campo
        // oculto con el token (integración con RequestDataValueProcessor):
        // no hace falta tocar ningún formulario existente.

        return http.build();
    }

}
