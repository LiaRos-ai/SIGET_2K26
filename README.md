# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 2 (Día 9) — Spring Security: autenticación y cuentas

Novedades del Día 9 respecto del Día 8:

- **Nueva entidad `Usuario`** (`@Entity`, tabla `usuarios`): `nombre`,
  `email` (único), `password` (hash, nunca texto plano) y `rol`
  (`"USER"` o `"ADMIN"`).
- **`CustomUserDetailsService`** (paquete nuevo `security/`): traduce un
  `Usuario` de la base de datos al `UserDetails` que Spring Security
  entiende, buscándolo por email.
- **`UsuarioService`**: la contraseña se encripta con
  `BCryptPasswordEncoder` antes de guardarse. El registro público
  (`registrar(nombre, email, password)`) siempre asigna el rol `"USER"`
  — nadie puede autoasignarse `"ADMIN"` desde el formulario, porque
  `RegistroFormDTO` ni siquiera tiene ese campo.
- **`GET/POST /registro`** (`AuthController`): formulario de registro
  con validación (`@NotBlank`, `@Email`, `@Size` mínimo 6 caracteres) y
  verificación de email duplicado.
- **`GET /login`**: vista de login propia (el `POST /login` lo
  intercepta Spring Security automáticamente vía `formLogin()`).
- **`SecurityConfig` real**: rutas públicas (`/`, `/login`, `/registro`,
  estáticos) con `permitAll`; `/admin/**` requiere `hasRole("ADMIN")`;
  cualquier otra ruta (incluido todo `/tareas/**`) requiere estar
  autenticado.
- **`/admin`** (`AdminController`): página mínima de ejemplo para
  mostrar la autorización por rol en acción.
- **`fragments/header.html`** ahora usa `sec:authorize` (de
  `thymeleaf-extras-springsecurity6`, dependencia ya incluida desde el
  Día 1) para mostrar Login/Registro sin sesión, o el email + botón
  "Salir" con sesión iniciada.
- **Cuentas de ejemplo** creadas por `DataInitializer` (protegidas contra
  duplicados en reinicios): `admin@tallerpw.com` / `admin1234` (ADMIN) y
  `estudiante@tallerpw.com` / `estudiante1234` (USER).
- **CSRF sigue deshabilitado** por ahora: se aborda en profundidad el
  Día 10, junto con sesiones y protección de rutas más fina por rol.

## Cómo ejecutar

```bash
mvn spring-boot:run
```

- `http://localhost:8080/` → pública.
- `http://localhost:8080/tareas` → ahora exige login (redirige a `/login` si no hay sesión).
- `http://localhost:8080/login` → probar con `admin@tallerpw.com` / `admin1234` o `estudiante@tallerpw.com` / `estudiante1234`.
- `http://localhost:8080/admin` → solo accesible logueado como `admin@tallerpw.com` (rol ADMIN); con el usuario `estudiante` da error 403.
- `http://localhost:8080/registro` → crear una cuenta nueva (siempre con rol USER).

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
(ej. `Sprint 2: agrega autenticación con Spring Security`).

## Product Backlog (actualizado — Sprint 2, Día 9)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-01 | Página de inicio | Alta | Sprint 0 | Hecho (Día 1) |
| HU-02 | Listado de tareas responsive | Alta | Sprint 1 | Hecho (Día 3-4) |
| HU-03 | Crear tarea desde formulario validado | Alta | Sprint 1 | Hecho (Día 5) |
| HU-04 | Editar y eliminar una tarea existente | Alta | Sprint 2 | Hecho (Día 8) |
| HU-05 | Persistencia real en base de datos | Alta | Sprint 2 | Hecho (Día 7) |
| HU-06 | Registro e inicio de sesión | Alta | Sprint 2 | Hecho (Día 9) |
| HU-07 | Vista de administrador (todas las tareas) | Media | Sprint 2 | En progreso (rol ADMIN y ruta protegida ya existen; falta la vista real) |
| HU-08 | Filtrar tareas por estado | Media | Sprint 2 | Hecho (Día 6) |
| HU-09 | Endpoint REST de tareas | Alta | Sprint 3 | Backlog |
| HU-10 | Seguridad transaccional | Alta | Sprint 3 | Backlog (parcial: DTO evita over-posting; contraseñas hasheadas) |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | Backlog |

Nota: las tareas del proyecto guía todavía NO están asociadas a un
Usuario dueño (eso implicaría agregar una relación Usuario-Tarea). Por
ahora, cualquier usuario autenticado ve y gestiona las mismas tareas —
la separación de "mis tareas" por usuario y el refinamiento de rutas por
rol se profundizan el Día 10.

## Tablero Scrum/Kanban

Mover HU-06 a "Hecho". Dejar HU-07 en "En progreso" para el Día 10.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 10:** Filtros, sesiones y rutas seguras — SecurityFilterChain
  más fino, protección de rutas por rol, manejo de sesión HTTP,
  protección CSRF. Cierre de Sprint 2 (Sprint Review, Retrospectiva y
  Evaluación semanal 2).





