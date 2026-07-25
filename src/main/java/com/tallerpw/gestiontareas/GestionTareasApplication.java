package com.tallerpw.gestiontareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto guía del curso Programación Web II.
 * "Sistema de Gestión de Tareas" - desarrollado en vivo por el docente
 * durante las 3 semanas del curso, sesión a sesión.
 *
 * Estado actual: Sprint 2 (Día 10) - Filtros, sesiones y rutas seguras. CIERRE DE SPRINT 2.
 * CSRF habilitado, manejo explícito de sesión (maximumSessions), y
 * autorización a nivel de datos (cada usuario ve/gestiona solo sus
 * tareas, salvo ADMIN que ve todas). HU-07 queda cerrada.
 */
@SpringBootApplication
public class GestionTareasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTareasApplication.class, args);
    }

}
