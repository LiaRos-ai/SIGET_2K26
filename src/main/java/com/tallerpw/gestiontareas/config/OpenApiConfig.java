package com.tallerpw.gestiontareas.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI/Swagger (Día 13).
 *
 * La dependencia springdoc-openapi-starter-webmvc-ui está en el pom.xml
 * desde el Día 1. Sin ningún código adicional, ya expone:
 *   - /v3/api-docs        (el JSON crudo de la especificación OpenAPI)
 *   - /swagger-ui/index.html (la interfaz visual para explorar y
 *     probar la API directamente desde el navegador)
 *
 * Este Bean solo agrega un título y una descripción legibles; no es
 * necesario para que Swagger funcione, pero mejora mucho la experiencia.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gestionTareasOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Tareas")
                        .description("Proyecto guía del curso Programación Web II — CRUD de tareas vía JSON, protegido con HTTP Basic.")
                        .version("v1"));
    }

}
