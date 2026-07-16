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

## Estado actual: Sprint 1 (Día 4) — Bootstrap, layouts y diseño responsive

Novedades del Día 4 respecto del Día 3:

- Se agregó `templates/fragments/header.html` (barra de navegación) y
  `templates/fragments/footer.html`, incluidos en todas las páginas con
  `th:replace="~{fragments/header :: header}"` — el `<div>` anfitrión
  desaparece y queda directamente el `<nav>`/`<footer>` del fragmento.
- Se agregó `templates/fragments/aviso.html`, un fragmento **parametrizado**
  (`th:fragment="aviso(mensaje)"`) incluido con `th:insert` en `tareas.html`
  — a diferencia de `th:replace`, el `<div>` anfitrión se conserva y el
  contenido del fragmento queda adentro.
- Todas las vistas (`inicio.html`, `tareas.html`, `tarea-detalle.html`,
  `tarea-no-encontrada.html`) se reescribieron para usar estos fragments,
  eliminando la duplicación de HTML.
- Se agregó diseño responsive real con clases de Bootstrap:
  `row-cols-1 row-cols-md-2 row-cols-lg-3` en el listado (1 columna en
  celular, 2 en tablet, 3 en escritorio) y `col-12 col-md-6` en las
  tarjetas de una sola columna.

- `http://localhost:8080/` → página de verificación (Día 1), con enlace al listado.
- `http://localhost:8080/tareas` → listado real con Thymeleaf (Día 3).
- `http://localhost:8080/tareas/1` → detalle de una tarea (probar también con un id que no exista, ej. `/tareas/99`).

## Cómo ejecutar

```bash
mvn clean
mvn spring-boot:run
```

Luego abrir `http://localhost:8080/` — debe mostrar la tarjeta de verificación
"¡Spring Boot está corriendo correctamente!".

Si el puerto 8080 está ocupado, cambiar `server.port` en `application.properties`.

## Product Backlog (actualizado — Sprint 1, cierre Día 4)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-01 | Como usuario quiero ver una página de inicio para confirmar que la app funciona | Alta | Sprint 0 | Hecho (Día 1) |
| HU-02 | Como usuario quiero ver un listado de tareas para saber qué pendientes existen | Alta | Sprint 1 | Hecho (Día 3) |
| HU-03 | Como usuario quiero crear una tarea nueva desde un formulario | Alta | Sprint 1 | Backlog (Día 5) |
| HU-04 | Como usuario quiero editar y eliminar una tarea existente | Alta | Sprint 1-2 | Backlog |
| HU-05 | Como usuario quiero que las tareas se guarden en base de datos (no en memoria) | Alta | Sprint 2 | Backlog |
| HU-06 | Como usuario quiero registrarme e iniciar sesión para tener mis propias tareas | Alta | Sprint 2 | Backlog |
| HU-07 | Como administrador quiero ver las tareas de todos los usuarios | Media | Sprint 2 | Backlog |
| HU-08 | Como usuario quiero marcar una tarea como completada y filtrar por estado | Media | Sprint 2 | Backlog |
| HU-09 | Como sistema quiero exponer un endpoint REST de tareas para integraciones externas | Alta | Sprint 3 | Backlog |
| HU-10 | Como usuario quiero que mis datos estén protegidos (validaciones, XSS, transacciones) | Alta | Sprint 3 | Backlog |
| HU-11 | Como equipo queremos empaquetar y desplegar la aplicación como JAR ejecutable | Alta | Sprint 3 | Backlog |

Nota: HU-02 ya incluye ahora el diseño responsive (Día 4), así que se considera
totalmente cerrada. No se agrega una historia nueva para "responsive" porque
formaba parte del criterio de aceptación original.

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
