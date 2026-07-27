package com.tallerpw.gestiontareas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad.
 *
 * Día 1-8: todo abierto (permitAll).
 * Día 9: autenticación real — login, registro, BCrypt, roles.
 * Día 10: CSRF habilitado, manejo explícito de sesión, autorización a
 *         nivel de datos (HU-07).
 * Día 11: /api/** quedó abierta y sin CSRF, a propósito y temporalmente.
 *
 * Día 12: se protege la API, con un enfoque DISTINTO al de las vistas
 * Thymeleaf, porque un cliente de API (Postman, una app externa) no
 * inicia sesión con un formulario ni maneja cookies de la misma forma
 * que un navegador. La solución: DOS SecurityFilterChain separadas,
 * cada una con su propio securityMatcher:
 *
 *   1. apiFilterChain (@Order(1)): solo procesa /api/**. Usa HTTP Basic
 *      (usuario y contraseña van en el header Authorization de CADA
 *      petición, sin sesión ni cookies) y SessionCreationPolicy.STATELESS
 *      (nunca crea ni usa sesión HTTP para estas rutas).
 *   2. webFilterChain (@Order(2)): procesa todo lo demás. Es exactamente
 *      la configuración que ya conocíamos desde el Día 10 (formLogin,
 *      sesión, CSRF), sin ningún cambio, más las rutas de Swagger UI
 *      agregadas el Día 13.
 *
 * Cuando llega una petición, Spring Security evalúa las cadenas en
 * orden: si la ruta coincide con el securityMatcher de apiFilterChain
 * (/api/**), usa esa; si no, sigue a webFilterChain.
 *
 * Día 13: se agregan /swagger-ui/** y /v3/api-docs/** a las rutas
 * públicas, para poder explorar la documentación de la API sin iniciar
 * sesión (la API en sí sigue protegida con HTTP Basic al probarla).
 */
@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Cadena para la API REST. HTTP Basic es el mecanismo más simple de
     * Spring Security para proteger una API: el cliente manda
     * "Authorization: Basic <usuario:contraseña en base64>" en cada
     * petición. No hace falta un formulario de login propio para la API:
     * Postman tiene una pestaña "Authorization" con un modo "Basic Auth"
     * que arma ese header automáticamente.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .httpBasic(basic -> {}) // habilita HTTP Basic con su configuración por defecto
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // CSRF no aplica a APIs sin estado autenticadas por header:
            // no hay cookie de sesión que un sitio malicioso pueda
            // aprovechar (ver la Guía de Filtros, Sesiones y CSRF del Día 10).
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    /**
     * Cadena para el resto de la aplicación (vistas Thymeleaf). Es la
     * misma configuración de los Días 9-10, sin cambios.
     */
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/registro", "/css/**", "/js/**", "/webjars/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
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
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            );
        // CSRF sigue HABILITADO acá (no se llama a .csrf(...disable...)).
        // Thymeleaf inserta el token automáticamente en <form th:action="...">.

        return http.build();
    }

}
