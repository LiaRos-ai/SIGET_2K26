# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

### Estado actual: Sprint 2 (Día 7) — Persistencia con Spring Data JPA

Novedades del Día 7 respecto del Día 6:

- **`Tarea` ahora es una entidad JPA real**: `@Entity`, `@Table(name = "tareas")`,
  `@Id` + `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
- **Nueva entidad `Categoria`** (`@Entity`, tabla `categorias`), para
  demostrar una relación real: una Categoria tiene muchas Tareas
  (`@OneToMany(mappedBy = "categoria")`) y cada Tarea pertenece
  opcionalmente a una Categoria (`@ManyToOne` + `@JoinColumn`).
- **`TareaRepositoryEnMemoria` (Días 2-6) fue eliminada.**
  `TareaRepository` ahora extiende `JpaRepository<Tarea, Long>`, y Spring
  Data JPA genera la implementación real contra la base de datos — ya no
  hace falta escribir la clase de implementación a mano.
- Se agregó `CategoriaRepository extends JpaRepository<Categoria, Long>`.
- `listarFiltradas` usa ahora `findByCompletada(boolean)`, un **query
  method**: Spring Data JPA arma la consulta SQL/JPQL a partir del nombre
  del método.
- `application.properties` tiene la configuración real de conexión a
  MySQL (con alternativa comentada para PostgreSQL), `ddl-auto=update`
  (Hibernate crea/actualiza las tablas solo) y `show-sql=true` para ver
  el SQL generado en la consola.
- Cuidado con Lombok y relaciones bidireccionales: `Categoria` usa
  `@Getter/@Setter` (no `@Data`) para no generar un `toString`/`equals`
  que recorra la lista de tareas; `Tarea` excluye el campo `categoria`
  de su `toString`/`equals` con `@ToString.Exclude` /
  `@EqualsAndHashCode.Exclude`. Sin esto, se corre el riesgo de un
  `StackOverflowError` por recursión infinita.

## Cómo ejecutar

Requiere un servidor MySQL corriendo localmente (ver `application.properties`).

```bash
mvn spring-boot:run
```

- Hibernate crea las tablas `tareas` y `categorias` automáticamente al arrancar.
- `http://localhost:8080/tareas` → el listado ahora muestra la categoría de cada tarea.
- Mirar la consola: `show-sql=true` imprime el SQL real que genera cada operación.

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
(ej. `Sprint 2: agrega persistencia real con Spring Data JPA`).

## Sprint 2 Planning (definido el Día 6)

| Entidad | Atributos previstos | Estado |
|---|---|---|
| Tarea | id, titulo, completada, categoria | Implementada (Día 7) |
| Categoria | id, nombre, tareas | Implementada (Día 7) |
| Usuario | id, nombre, email, password, rol | Pendiente (Día 9, Spring Security) |

Cada equipo de estudiantes debe modelar de la misma forma las entidades JPA
de su propio proyecto, incluyendo al menos una relación `@OneToMany`/`@ManyToOne`
(ver la tabla de entidades sugeridas en la "Guía General para los 5 Proyectos").

## Product Backlog (actualizado — Sprint 2, Día 7)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-01 | Página de inicio | Alta | Sprint 0 | Hecho (Día 1) |
| HU-02 | Listado de tareas responsive | Alta | Sprint 1 | Hecho (Día 3-4) |
| HU-03 | Crear tarea desde formulario validado | Alta | Sprint 1 | Hecho (Día 5) |
| HU-04 | Editar y eliminar una tarea existente | Alta | Sprint 2 | En progreso (toggle ya implementado; CRUD completo el Día 8) |
| HU-05 | Persistencia real en base de datos | Alta | Sprint 2 | Hecho (Día 7) |
| HU-06 | Registro e inicio de sesión | Alta | Sprint 2 | Backlog (Día 9) |
| HU-07 | Vista de administrador (todas las tareas) | Media | Sprint 2 | Backlog |
| HU-08 | Filtrar tareas por estado | Media | Sprint 2 | Hecho (Día 6) |
| HU-09 | Endpoint REST de tareas | Alta | Sprint 3 | Backlog |
| HU-10 | Seguridad transaccional | Alta | Sprint 3 | Backlog (parcial: DTO evita over-posting) |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | Backlog |

## Tablero Scrum/Kanban

Mover HU-05 a "Hecho" y preparar HU-04 (CRUD completo) para el Daily del Día 8.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 8:** CRUD completo (crear, leer, actualizar, eliminar) integrado
  de punta a punta con las vistas Thymeleaf ya existentes, usando Lombok
  para reducir código repetitivo.

