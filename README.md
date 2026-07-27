# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 3 (Día 13) — Calidad, manejo de errores y pruebas

Novedades del Día 13 respecto del Día 12:

- **Manejo global de excepciones**, separado por tipo de respuesta:
  - `RecursoNoEncontradoException` (paquete `exception/`): reemplaza los
    `.map(...).orElse("tarea-no-encontrada")` y
    `ResponseEntity.notFound().build()` repetidos en varios métodos.
  - `TareaWebExceptionHandler` (`@ControllerAdvice(assignableTypes = TareaController.class)`):
    la captura para las vistas y muestra `tarea-no-encontrada.html`.
  - `ApiExceptionHandler` (`@RestControllerAdvice(assignableTypes = TareaRestController.class)`):
    la captura para la API y devuelve un `ErrorResponseDTO` con 404. También
    centraliza `MethodArgumentNotValidException` (errores de `@Valid` en
    JSON) con 400, y cualquier otra excepción no prevista con 500.
  - `GlobalWebExceptionHandler` (`@ControllerAdvice` sin restricción): red
    de seguridad para cualquier error inesperado en cualquier `@Controller`,
    muestra `error.html` con un mensaje genérico (nunca un stack trace).
- **Logging con SLF4J** agregado en `TareaService`, `AuthController` y
  los `ExceptionHandler` (`log.info` para operaciones exitosas, `log.warn`
  para intentos sin permiso o datos duplicados, `log.error` para fallas
  reales). `application.properties` ahora define un patrón de log
  explícito y vuelca una copia a `logs/gestion-tareas.log`.
- **Pruebas unitarias con JUnit 5 + Mockito**: `TareaServiceTest`
  (`src/test/java`), con `@Mock`/`@InjectMocks` sobre `TareaRepository`
  y `CategoriaService`, cubriendo creación, permisos (dueño/ADMIN/sin
  permiso) y casos de "no existe".
- **Documentación con Swagger/OpenAPI**: `OpenApiConfig` agrega título y
  descripción; `TareaRestController` suma `@Tag`/`@Operation`. La
  dependencia `springdoc-openapi-starter-webmvc-ui` estaba en el
  `pom.xml` desde el Día 1 — hoy se usa por primera vez.

## Cómo ejecutar

```bash
mvn spring-boot:run
```

- `http://localhost:8080/swagger-ui/index.html` → documentación interactiva de la API (pública, sin login).
- `http://localhost:8080/tareas/99999` → ya no revienta con un error genérico: muestra "tarea no encontrada".
- `http://localhost:8080/api/tareas/99999` (con Basic Auth) → 404 con un JSON `{"status":404,"mensaje":"..."}`.
- `mvn test` → corre `TareaServiceTest` (no requiere base de datos: todo está mockeado).
- Revisar `logs/gestion-tareas.log` después de correr la app, para ver el archivo de log generado.

## Configuración de Git (actividad del Día 2)

```bash
cd gestion-tareas
git init
git add .
git commit -m "Sprint 0: proyecto base + capas Controller-Service-Repository (Día 1-2)"
git branch -M main
git remote add origin <URL-del-repositorio-remoto>
git push -u origin main
```

Convención de commits sugerida para todo el curso: `Sprint X: descripción breve en presente`
(ej. `Sprint 3: centraliza el manejo de errores y agrega pruebas unitarias`).

## HTTPS (nota conceptual, sin cambios de código)

En desarrollo local no configuramos HTTPS: alcanza con `http://localhost`.
En un despliegue real, HTTPS es indispensable. Dos formas típicas de
habilitarlo: certificado directo en Spring Boot (`server.ssl.*`) o
delegado a un proxy inverso (Nginx, balanceador del hosting) — el
enfoque que se retoma en el Día 14 (despliegue).

## Product Backlog (actualizado — Sprint 3, Día 13)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-09 | Endpoint REST de tareas | Alta | Sprint 3 | Hecho (Día 11) |
| HU-10 | Seguridad transaccional | Alta | Sprint 3 | Hecho (Día 12) |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | Backlog (Día 14) |
| (técnica) | Manejo de errores, logging, pruebas, documentación de API | Alta | Sprint 3 | **Hecho (Día 13)** |

## Tablero Scrum/Kanban

Agregar la tarea técnica del Día 13 como "Hecho". Preparar HU-11
(empaquetado y despliegue) para el Día 14.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 14:** Empaquetado con Maven (`mvn package`) a JAR ejecutable,
  variables de entorno por perfil (`application-prod.properties`),
  despliegue en servidor o contenedor Docker.
