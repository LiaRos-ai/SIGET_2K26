package com.tallerpw.gestiontareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto guía del curso Programación Web II.
 * "Sistema de Gestión de Tareas" - desarrollado en vivo por el docente
 * durante las 3 semanas del curso, sesión a sesión.
 *
 * Estado actual: Sprint 3 (Día 13) - Calidad, manejo de errores y pruebas.
 * Manejo global de excepciones (@ControllerAdvice/@RestControllerAdvice
 * separados para vistas y API), logging con SLF4J/Logback, pruebas
 * unitarias con JUnit 5 + Mockito (TareaServiceTest), y documentación
 * de la API con Swagger/OpenAPI en /swagger-ui/index.html.
 */
@SpringBootApplication
public class GestionTareasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTareasApplication.class, args);
    }

}
