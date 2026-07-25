# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 2 (Día 10) — Filtros, sesiones y rutas seguras · CIERRE DE SPRINT 2

Novedades del Día 10 respecto del Día 9:

- **CSRF habilitado**: hasta el Día 9, `SecurityConfig` terminaba con
  `.csrf(csrf -> csrf.disable())`. Ahora ese bloque directamente no
  aparece, así que Spring Security usa la protección CSRF por defecto
  (token asociado a la sesión HTTP). Ningún formulario existente
  necesitó cambios: Thymeleaf inserta el campo oculto automáticamente
  en cualquier `<form th:action="...">`.
- **Manejo explícito de sesión**: `sessionCreationPolicy(IF_REQUIRED)`
  (el valor por defecto, ahora visible en el código) y
  `maximumSessions(1)` — cada usuario tiene como máximo una sesión
  activa a la vez.
- **HU-07 cerrada: autorización a nivel de DATOS.** Se agregó
  `Tarea.propietario` (Usuario dueño). `GET /tareas` ahora muestra:
  - Todas las tareas de todos los usuarios, si es ADMIN.
  - Solo tus propias tareas, si es un usuario normal.
  - Editar, eliminar y alternar el estado de una tarea ahora verifican
    la propiedad (`TareaService.puedeGestionar`): un usuario normal solo
    puede gestionar sus propias tareas; un ADMIN puede gestionar
    cualquiera.
- `DataInitializer` se reordenó: ahora crea los usuarios de ejemplo
  ANTES que las tareas, para poder asignarles un propietario desde el
  arranque, y usa `obtenerOCrear` para no fallar si ya existen (además
  de la verificación de email duplicado del Día 9).

## Cómo ejecutar

```bash
mvn spring-boot:run
```

- Iniciar sesión como `estudiante@tallerpw.com` / `estudiante1234` → `/tareas` muestra solo sus 3 tareas de ejemplo.
- Iniciar sesión como `admin@tallerpw.com` / `admin1234` → `/tareas` muestra un aviso "viendo las tareas de todos los usuarios" y las lista todas, con el nombre del propietario en cada tarjeta.
- Probar editar/eliminar una tarea ajena manipulando la URL manualmente (por ejemplo, `/tareas/1/editar` estando logueado con otra cuenta que no sea dueña ni ADMIN) → debería mostrar "tarea no encontrada", no un error.

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
(ej. `Sprint 2: cierra Sprint 2 con CSRF, sesiones y tareas por usuario`).

## Product Backlog (actualizado — cierre de Sprint 2, Día 10)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-01 | Página de inicio | Alta | Sprint 0 | Hecho (Día 1) |
| HU-02 | Listado de tareas responsive | Alta | Sprint 1 | Hecho (Día 3-4) |
| HU-03 | Crear tarea desde formulario validado | Alta | Sprint 1 | Hecho (Día 5) |
| HU-04 | Editar y eliminar una tarea existente | Alta | Sprint 2 | Hecho (Día 8) |
| HU-05 | Persistencia real en base de datos | Alta | Sprint 2 | Hecho (Día 7) |
| HU-06 | Registro e inicio de sesión | Alta | Sprint 2 | Hecho (Día 9) |
| HU-07 | Vista de administrador (todas las tareas) | Media | Sprint 2 | **Hecho (Día 10)** |
| HU-08 | Filtrar tareas por estado | Media | Sprint 2 | Hecho (Día 6) |
| HU-09 | Endpoint REST de tareas | Alta | Sprint 3 | Backlog (Día 11) |
| HU-10 | Seguridad transaccional | Alta | Sprint 3 | Backlog (parcial: DTO, contraseñas hasheadas, CSRF, autorización por datos) |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | Backlog |

**Sprint 2 cerrado.** Incremento entregado: entidades JPA y repositorios,
CRUD completo conectado al frontend, autenticación con Spring Security,
roles y rutas protegidas (coincide con el incremento planeado para el
Sprint 2 en el plan de curso).

## Sprint Review y Retrospectiva 2 (actividad del Día 10)

- **Sprint Review:** cada estudiante/equipo muestra su proyecto de práctica
  corriendo, con login, CRUD completo, y al menos una ruta protegida por rol.
- **Retrospectiva:** ¿qué funcionó bien esta semana (Días 6-10)? ¿qué fue
  difícil? ¿qué cambiaríamos para la semana 3 (Sprint 3: API REST y despliegue)?

## Tablero Scrum/Kanban

Mover HU-07 a "Hecho". Preparar el Sprint 3 Planning (Día 11) con HU-09, HU-10 y HU-11.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 11:** Sprint 3 Planning — API REST con Spring Boot
  (`@RestController`, `@RequestBody`/`@ResponseBody`, serialización JSON,
  pruebas con Postman).

