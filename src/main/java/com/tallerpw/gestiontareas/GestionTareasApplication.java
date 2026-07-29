package com.tallerpw.gestiontareas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Proyecto guía del curso Programación Web II.
 * "Sistema de Gestión de Tareas" - desarrollado en vivo por el docente
 * durante las 3 semanas del curso, sesión a sesión.
 *
 * Estado actual: Sprint 3 (Día 14) - Empaquetado y despliegue.
 * application.properties se dividió en perfiles (dev/prod), con
 * Dockerfile multi-etapa y docker-compose.yml (app + MySQL) listos
 * para desplegar el mismo JAR generado con "mvn package".
 */
@SpringBootApplication
public class GestionTareasApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionTareasApplication.class, args);
    }

}
