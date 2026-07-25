package com.tallerpw.gestiontareas.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad real (Día 9).
 *
 * Desde el Día 1 hasta el Día 8, esta clase dejaba todo abierto
 * (permitAll en cualquier ruta) para no bloquear el desarrollo de las
 * vistas antes de tiempo. A partir de hoy:
 *
 *   - Las rutas públicas (home, login, registro, estáticos) quedan con
 *     permitAll.
 *   - /admin/** requiere el rol ADMIN.
 *   - Cualquier otra ruta (incluida /tareas/**) requiere estar autenticado.
 *   - Se configura un formulario de login propio (no el genérico de
 *     Spring Security) y el logout.
 *
 * CSRF sigue deshabilitado por ahora: se aborda en profundidad el Día 10
 * ("Filtros, sesiones y rutas seguras"), junto con el manejo de sesión.
 * La protección real de rutas POR ROL (más allá de este ejemplo simple
 * con /admin) también se profundiza ese día.
 */
@Configuration
public class SecurityConfig {

    /**
     * BCryptPasswordEncoder aplica un algoritmo de hash lento a propósito
     * (para dificultar ataques de fuerza bruta) y agrega automáticamente
     * un "salt" distinto en cada contraseña, aunque dos usuarios elijan
     * la misma clave.
     */
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
            .csrf(csrf -> csrf.disable()); // se revisa en profundidad el Día 10

        return http.build();
    }

}
