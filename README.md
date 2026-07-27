# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 3 (Día 11) — API REST con Spring Boot

Novedades del Día 11 respecto del Día 10:

- **Nuevo `TareaRestController`** (`@RestController`, base `/api/tareas`):
  CRUD completo vía JSON.
  - `GET /api/tareas` y `GET /api/tareas?completada=true|false`
  - `GET /api/tareas/{id}` (200 o 404)
  - `POST /api/tareas` (201, body: `{"titulo": "...", "categoriaId": 1}`)
  - `PUT /api/tareas/{id}` (200 o 404)
  - `DELETE /api/tareas/{id}` (204 o 404)
- **`TareaResponseDTO` y `TareaRequestDTO`** (paquete `dto/`): la API
  nunca serializa las entidades JPA directamente — el mismo principio
  de los DTO de formularios (Día 6), aplicado a JSON.
- **`TareaService` ganó 3 métodos nuevos** (`crearDesdeApi`,
  `actualizarDesdeApi`, `eliminarDesdeApi`) que, a diferencia de los
  usados por la capa web, NO aplican la verificación de propietario
  (`puedeGestionar`) — porque la API todavía no tiene su propio
  mecanismo de autenticación.
- **`/api/**` está abierta (`permitAll`) y exenta de CSRF**, a
  propósito y de forma temporal: protegerla correctamente es el
  contenido del Día 12.

## Cómo ejecutar

```bash
mvn spring-boot:run
```

Probar con Postman (no requiere login, a diferencia de `/tareas`):

- `GET http://localhost:8080/api/tareas`
- `GET http://localhost:8080/api/tareas?completada=true`
- `POST http://localhost:8080/api/tareas` con body JSON:
  ```json
  { "titulo": "Probar la API con Postman", "categoriaId": 1 }
  ```
- `PUT http://localhost:8080/api/tareas/1` con un body similar.
- `DELETE http://localhost:8080/api/tareas/1`

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
(ej. `Sprint 3: agrega API REST de tareas`).

## Sprint 3 Planning (actividad del Día 11)

Al arrancar el Sprint 3, se define qué entidad del proyecto propio se
expone como API REST. Para el proyecto guía: la entidad Tarea, ya
expuesta hoy en `/api/tareas`. Cada equipo debe elegir su entidad
principal y definir al menos los 5 endpoints estándar (listar, detalle,
crear, actualizar, eliminar), documentando la forma esperada del JSON de
entrada y salida (equivalente a TareaRequestDTO/TareaResponseDTO).

## Product Backlog (actualizado — Sprint 3, Día 11)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-01 a HU-08 | (ver Sprints 0-2) | — | — | Hechas |
| HU-09 | Endpoint REST de tareas para integraciones externas | Alta | Sprint 3 | Hecho (Día 11) |
| HU-10 | Seguridad transaccional (incluye proteger la API) | Alta | Sprint 3 | Backlog (Día 12) |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | Backlog (Día 14) |

## Tablero Scrum/Kanban

Mover HU-09 a "Hecho". Dejar registrado en el backlog que `/api/tareas`
todavía no está protegida — esa tarea técnica se resuelve el Día 12.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 12:** Seguridad transaccional — `@Transactional`, prevención de
  inyección SQL/XSS, protección de la API con JWT o Spring Security, HTTPS.

