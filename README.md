# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 2 (Día 8) — CRUD completo e integración Front-Back

Novedades del Día 8 respecto del Día 7:

- **Se agregó `CategoriaService`**: `TareaService` ya no dependería
  directamente de `CategoriaRepository`; siempre pasa por su propio
  Service, manteniendo la regla de capas del Día 2.
- **`TareaFormDTO` ahora incluye `categoriaId`**: el formulario tiene un
  `<select>` para elegir la categoría de la tarea.
- **CRUD completo**:
  - `GET /tareas/{id}/editar` → formulario precargado con los datos actuales.
  - `POST /tareas/{id}/editar` → guarda los cambios (`actualizarDesdeFormulario`).
  - `POST /tareas/{id}/eliminar` → elimina la tarea (`eliminar`, con
    confirmación en el navegador vía `onsubmit="return confirm(...)"`).
- **`tarea-form.html` se reutiliza para crear y editar**: el atributo
  `editando` (booleano) cambia el título de la página y a qué URL apunta
  el `<form>` (`th:action`).
- El listado (`tareas.html`) y el detalle (`tarea-detalle.html`) ahora
  tienen botones "Editar" y "Eliminar".
- **HU-04 queda cerrada**: crear, leer, actualizar y eliminar tareas
  funciona de punta a punta, conectado a la base de datos real desde
  el Día 7.

## Cómo ejecutar

```bash
mvn spring-boot:run
```

- `http://localhost:8080/tareas` → listado con botones Editar/Eliminar en cada tarjeta.
- `http://localhost:8080/tareas/nueva` → crear, con selector de categoría.
- `http://localhost:8080/tareas/{id}/editar` → editar una tarea existente.

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
(ej. `Sprint 2: completa el CRUD de tareas con editar y eliminar`).

## Product Backlog (actualizado — Sprint 2, Día 8)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-01 | Página de inicio | Alta | Sprint 0 | Hecho (Día 1) |
| HU-02 | Listado de tareas responsive | Alta | Sprint 1 | Hecho (Día 3-4) |
| HU-03 | Crear tarea desde formulario validado | Alta | Sprint 1 | Hecho (Día 5) |
| HU-04 | Editar y eliminar una tarea existente | Alta | Sprint 2 | **Hecho (Día 8)** |
| HU-05 | Persistencia real en base de datos | Alta | Sprint 2 | Hecho (Día 7) |
| HU-06 | Registro e inicio de sesión | Alta | Sprint 2 | Backlog (Día 9) |
| HU-07 | Vista de administrador (todas las tareas) | Media | Sprint 2 | Backlog |
| HU-08 | Filtrar tareas por estado | Media | Sprint 2 | Hecho (Día 6) |
| HU-09 | Endpoint REST de tareas | Alta | Sprint 3 | Backlog |
| HU-10 | Seguridad transaccional | Alta | Sprint 3 | Backlog (parcial: DTO evita over-posting) |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | Backlog |

## Tablero Scrum/Kanban

Mover HU-04 a "Hecho". Preparar HU-06 (registro/login con Spring Security) para el Daily del Día 9.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 9:** Spring Security — autenticación, `UserDetailsService`,
  registro/login, `BCryptPasswordEncoder`, roles y autorización por endpoint.



