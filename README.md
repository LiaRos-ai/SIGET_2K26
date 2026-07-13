# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 0 (Día 1-2) — Planificación

Este repositorio contiene únicamente el **arranque del proyecto**:

- Proyecto Spring Boot generado con Spring Initializr (Java 17, Maven).
- Dependencias base: Web, Thymeleaf, Spring Data JPA, Spring Security, Validation,
  Lombok, DevTools, springdoc-openapi.
- Estructura de paquetes por capas: `controller`, `service`, `repository`, `model`, `config`.
- Un `HomeController` + vista `inicio.html` que solo sirve para **verificar que el
  entorno quedó bien instalado** (JDK, Maven, IDE, Spring Boot, Thymeleaf).
- `SecurityConfig` temporal que deja todo abierto (`permitAll`) hasta el Día 9,
  cuando se implemente la autenticación real.

Las carpetas `service/`, `repository/` y `model/` están vacías a propósito
(solo tienen un `.gitkeep`): se irán llenando desde el Día 2 en adelante.

## Estado actual: Sprint 0 (Día 2) — Anotaciones y capas

Novedades del Día 2 respecto del Día 1:

- Se agregó el modelo `Tarea` (POJO simple, sin JPA todavía).
- Se agregó la capa `repository` con `TareaRepository` (interfaz) y
  `TareaRepositoryEnMemoria` (implementación @Repository en memoria —
  los datos se pierden al reiniciar; eso se resuelve el Día 7 con JPA).
- Se agregó la capa `service` con `TareaService` (@Service), que recibe el
  repositorio por inyección de dependencias en el constructor (@Autowired).
- Se agregó `TareaController` (@Controller) en `/tareas`, que demuestra el
  recorrido completo Controller → Service → Repository. La respuesta es
  texto plano temporal (@ResponseBody); se reemplaza por una vista
  Thymeleaf real el Día 3.
- Se agregó `DataInitializer` (CommandLineRunner) que precarga 3 tareas de
  ejemplo al arrancar, para poder ver algo en `/tareas` sin necesidad de
  formularios todavía.

## Estado actual: Sprint 1 (Día 3) — Vistas con Thymeleaf

Novedades del Día 3 respecto del Día 2:

- El endpoint `/tareas` ya NO devuelve texto plano: `TareaController` ahora
  llena un `Model` y delega el renderizado a `templates/tareas.html`, que
  usa `th:each` para recorrer la lista, y `th:if` / `th:unless` para
  mostrar el estado (completada/pendiente) y el mensaje de lista vacía.
- Se agregó `GET /tareas/{id}` (`@PathVariable`) con dos vistas nuevas:
  `tarea-detalle.html` (si la tarea existe) y `tarea-no-encontrada.html`
  (si el `Optional` viene vacío).
- Se agregó `TareaService.buscarPorId(id)`.
- `inicio.html` (Día 1) ahora enlaza al listado de tareas.
- Todas las vistas usan Bootstrap (vía CDN) para un estilo responsive
  mínimo, aunque el layout con `th:fragment` / `th:replace` recién se
  construye el Día 4.

- `http://localhost:8080/` → página de verificación (Día 1), con enlace al listado.
- `http://localhost:8080/tareas` → listado real con Thymeleaf (Día 3).
- `http://localhost:8080/tareas/1` → detalle de una tarea (probar también con un id que no exista, ej. `/tareas/99`).

## Cómo ejecutar

```bash
mvn spring-boot:run
```

Luego abrir `http://localhost:8080/` — debe mostrar la tarjeta de verificación
"¡Spring Boot está corriendo correctamente!".

Si el puerto 8080 está ocupado, cambiar `server.port` en `application.properties`.

## Product Backlog inicial (Sprint 0 — construido en clase el Día 1)

Historias de usuario de referencia para arrancar el tablero Scrum/Kanban del
proyecto guía. El docente las escribe/discute en vivo con el curso (rol Product Owner);
el número de Historia y la prioridad pueden ajustarse según lo que surja en el Planning.

| # | Historia de usuario | Prioridad | Sprint estimado |
|---|---|---|---|
| HU-01 | Como usuario quiero ver una página de inicio para confirmar que la app funciona | Alta | Sprint 0 |
| HU-02 | Como usuario quiero ver un listado de tareas para saber qué pendientes existen | Alta | Sprint 1 |
| HU-03 | Como usuario quiero crear una tarea nueva desde un formulario | Alta | Sprint 1 |
| HU-04 | Como usuario quiero editar y eliminar una tarea existente | Alta | Sprint 1-2 |
| HU-05 | Como usuario quiero que las tareas se guarden en base de datos (no en memoria) | Alta | Sprint 2 |
| HU-06 | Como usuario quiero registrarme e iniciar sesión para tener mis propias tareas | Alta | Sprint 2 |
| HU-07 | Como administrador quiero ver las tareas de todos los usuarios | Media | Sprint 2 |
| HU-08 | Como usuario quiero marcar una tarea como completada y filtrar por estado | Media | Sprint 2 |
| HU-09 | Como sistema quiero exponer un endpoint REST de tareas para integraciones externas | Alta | Sprint 3 |
| HU-10 | Como usuario quiero que mis datos estén protegidos (validaciones, XSS, transacciones) | Alta | Sprint 3 |
| HU-11 | Como equipo queremos empaquetar y desplegar la aplicación como JAR ejecutable | Alta | Sprint 3 |

## Tablero Scrum/Kanban

Columnas sugeridas: `Backlog` · `Sprint Backlog` · `En progreso` · `En revisión` · `Hecho`.
Usar Trello, GitHub Projects o un tablero físico — lo importante es que se actualice
en el Daily de 5 minutos al inicio de cada sesión.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes (tanto en este proyecto guía, de forma
  colectiva/observada, como en sus proyectos propios, en equipos).

## Próximos hitos

- **Día 2:** anotaciones y capas (`@Controller`, `@Service`, `@Repository`), repo Git
  del proyecto guía.
- **Día 3-4:** vistas Thymeleaf + Bootstrap.
- **Día 5:** formularios + validación, cierre de Sprint 1, elección de proyecto
  por parte de los estudiantes.
