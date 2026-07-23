package com.tallerpw.gestiontareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto guía del curso Programación Web II.
 * "Sistema de Gestión de Tareas" - desarrollado en vivo por el docente
 * durante las 3 semanas del curso, sesión a sesión.
 *
 * Estado actual: Sprint 2 (Día 8) - CRUD completo e integración Front-Back.
 * Se completan las operaciones de actualizar y eliminar (editar/eliminar
 * una tarea desde la vista), con selector de categoría en el formulario.
 * HU-04 queda cerrada.
 */
@SpringBootApplication
public class GestionTareasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTareasApplication.class, args);
    }

}
