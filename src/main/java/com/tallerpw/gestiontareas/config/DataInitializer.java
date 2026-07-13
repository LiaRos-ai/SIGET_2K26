package com.tallerpw.gestiontareas.config;

import com.tallerpw.gestiontareas.service.TareaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Carga un par de tareas de ejemplo al arrancar la aplicación, para que
 * el Día 2 haya algo que ver/loguear sin necesidad de un formulario todavía
 * (los formularios llegan el Día 5).
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner cargarDatosDePrueba(TareaService tareaService) {
        return args -> {
            tareaService.crear("Configurar el repositorio Git del proyecto guía");
            tareaService.crear("Explicar @SpringBootApplication, @Controller, @Service y @Repository");
            tareaService.crear("Actualizar el tablero Kanban con el avance del Sprint 0");
        };
    }

}
