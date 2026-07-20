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

## Estado actual: Sprint 1 (Día 5) — Formularios y validación · CIERRE DE SPRINT 1

Novedades del Día 5 respecto del Día 4:

- Se agregaron anotaciones de Bean Validation a `Tarea`: `@NotBlank` y
  `@Size(min = 3, max = 100)` sobre `titulo`.
- Se agregó `GET /tareas/nueva` (muestra el formulario vacío) y
  `POST /tareas` (recibe el formulario) en `TareaController`.
- El método `crear` del Controller usa `@Valid` + `BindingResult`: si hay
  errores de validación, vuelve a mostrar el mismo formulario con los
  mensajes de error junto a cada campo (`th:errors`); si todo es válido,
  guarda la tarea y redirige a `/tareas` (`redirect:/tareas`).
- Nueva vista `tarea-form.html` con `th:object="${tarea}"` y
  `th:field="*{titulo}"` para el binding, más `th:errors="*{titulo}"`
  para mostrar el mensaje de validación.
- El listado (`tareas.html`) ahora tiene un botón "+ Nueva tarea".

- `http://localhost:8080/` → página de verificación (Día 1), con enlace al listado.
- `http://localhost:8080/tareas` → listado real con Thymeleaf (Día 3).
- `http://localhost:8080/tareas/1` → detalle de una tarea (probar también con un id que no exista, ej. `/tareas/99`).

## Cómo ejecutar

```bash
mvn clean
mvn spring-boot:run
```

## Estado actual: Sprint 2 (Día 6) — Spring MVC del lado backend

Novedades del Día 6 respecto del Día 5:

- Se agregó `dto/TareaFormDTO.java`: el formulario de creación ya NO
  bindea contra la entidad `Tarea` completa, sino contra este DTO que
  solo expone `titulo`. Esto evita el problema de **over-posting**
  (que alguien manipule campos ocultos como `id` o `completada` desde
  la petición HTTP) — ver el comentario en esa clase.
- `GET /tareas` ahora acepta un filtro opcional con `@RequestParam`:
  `/tareas?completada=true` o `/tareas?completada=false`. El listado
  muestra tres links (Todas / Pendientes / Completadas).
- Se agregó `POST /tareas/{id}/completar` (`@PathVariable`), que alterna
  el estado de una tarea. Es una operación de "actualización" simple,
  antes de llegar al CRUD completo del Día 8.
- `TareaService` ahora tiene `listarFiltradas(Boolean completada)` y
  `alternarCompletada(Long id)`.
- Nota sobre @PutMapping/@DeleteMapping: los formularios HTML solo
  soportan GET y POST de forma nativa, así que esta capa Thymeleaf sigue
  usando POST para "actualizar". Los verbos PUT/DELETE reales se usan
  desde el Día 11 con `@RestController` (Postman, JavaScript).

## Cómo ejecutar

```bash
mvn clean
mvn spring-boot:run
```

- `http://localhost:8080/tareas` → listado con filtro (Todas/Pendientes/Completadas).
- `http://localhost:8080/tareas?completada=true` → solo las completadas.
- Botón "↺" en cada tarjeta → alterna el estado (POST /tareas/{id}/completar).

Si el puerto 8080 está ocupado, cambiar `server.port` en `application.properties`.

## Product Backlog (actualizado — cierre de Sprint 1, Día 5)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-01 | Como usuario quiero ver una página de inicio para confirmar que la app funciona | Alta | Sprint 0 | Hecho (Día 1) |
| HU-02 | Como usuario quiero ver un listado de tareas para saber qué pendientes existen | Alta | Sprint 1 | Hecho (Día 3-4) |
| HU-03 | Como usuario quiero crear una tarea nueva desde un formulario | Alta | Sprint 1 | Hecho (Día 5) |
| HU-04 | Como usuario quiero editar y eliminar una tarea existente | Alta | Sprint 1-2 | Movida a Sprint 2 (Día 8, CRUD completo) |
| HU-05 | Como usuario quiero que las tareas se guarden en base de datos (no en memoria) | Alta | Sprint 2 | Backlog (Día 7) |
| HU-06 | Como usuario quiero registrarme e iniciar sesión para tener mis propias tareas | Alta | Sprint 2 | Backlog (Día 9) |
| HU-07 | Como administrador quiero ver las tareas de todos los usuarios | Media | Sprint 2 | Backlog |
| HU-08 | Como usuario quiero marcar una tarea como completada y filtrar por estado | Media | Sprint 2 | Backlog |
| HU-09 | Como sistema quiero exponer un endpoint REST de tareas para integraciones externas | Alta | Sprint 3 | Backlog |
| HU-10 | Como usuario quiero que mis datos estén protegidos (validaciones, XSS, transacciones) | Alta | Sprint 3 | Backlog (parcialmente iniciada: validación de formularios) |
| HU-11 | Como equipo queremos empaquetar y desplegar la aplicación como JAR ejecutable | Alta | Sprint 3 | Backlog |

**Sprint 1 cerrado.** Incremento entregado: proyecto Spring Boot con vistas Thymeleaf,
layout con fragments, diseño responsive, y formulario de creación con validación
(coincide con el incremento planeado para el Sprint 1 en el plan de curso).

## Sprint Review y Retrospectiva 1 (actividad del Día 5)

- **Sprint Review:** cada estudiante/equipo muestra su proyecto de práctica
  corriendo, con el listado, el detalle y el formulario de creación funcionando.
- **Retrospectiva:** responder en equipo — ¿qué funcionó bien esta semana?
  ¿qué fue difícil? ¿qué cambiaríamos para la semana 2? (ver plantilla en el
  documento "Guía de Bootstrap y Fragments" o en el plan de sesión del Día 5).

## Elección de proyecto (actividad del Día 5)

Al cierre de esta sesión, cada estudiante/equipo elige 1 de los 5 proyectos
propuestos (ver "Guía General para los 5 Proyectos de Estudiantes") y lo
declara formalmente completando la Ficha de Selección de Proyecto y Equipo.
A partir del Día 6 el proyecto guía del docente y los proyectos propios
avanzan en paralelo.