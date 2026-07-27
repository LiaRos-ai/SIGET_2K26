package com.tallerpw.gestiontareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto guía del curso Programación Web II.
 * "Sistema de Gestión de Tareas" - desarrollado en vivo por el docente
 * durante las 3 semanas del curso, sesión a sesión.
 *
 * Estado actual: Sprint 3 (Día 11) - API REST con Spring Boot.
 * Se agregó TareaRestController (@RestController) con el CRUD completo
 * de tareas en /api/tareas, usando @RequestBody/@ResponseBody, DTOs de
 * request/response y códigos HTTP explícitos. La API queda abierta a
 * propósito por ahora (se protege el Día 12).
 */
@SpringBootApplication
public class GestionTareasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTareasApplication.class, args);
    }

}
