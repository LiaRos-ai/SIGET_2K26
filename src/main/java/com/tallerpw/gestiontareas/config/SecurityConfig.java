package com.tallerpw.gestiontareas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad TEMPORAL.
 *
 * La dependencia spring-boot-starter-security ya está incluida en el pom.xml
 * desde el Día 1 (para no reconfigurar el proyecto más adelante), pero se
 * deja todo abierto hasta el Día 9 ("Spring Security: autenticación y
 * cuentas"), donde esta clase se reemplaza por la configuración real con
 * UserDetailsService, roles y BCryptPasswordEncoder.
 *
 * IMPORTANTE para el docente: recordar a los estudiantes que sin esta
 * clase, Spring Security bloquearía TODAS las rutas por defecto y pediría
 * un usuario "user" con una contraseña generada en consola.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .csrf(csrf -> csrf.disable()); // se reactivará junto con los formularios reales

        return http.build();
    }

}
