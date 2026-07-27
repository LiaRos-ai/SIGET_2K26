package com.tallerpw.gestiontareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto guía del curso Programación Web II.
 * "Sistema de Gestión de Tareas" - desarrollado en vivo por el docente
 * durante las 3 semanas del curso, sesión a sesión.
 *
 * Estado actual: Sprint 3 (Día 12) - Seguridad transaccional.
 * @Transactional real (CategoriaService.eliminarConTareas), API
 * protegida con HTTP Basic + sesión STATELESS (SecurityConfig con dos
 * SecurityFilterChain), y notas de seguridad sobre inyección SQL (JPA
 * parametriza todo por diseño) y XSS (th:text escapa automáticamente).
 */
@SpringBootApplication
public class GestionTareasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTareasApplication.class, args);
    }

}
