# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 3 (Día 12) — Seguridad transaccional

Novedades del Día 12 respecto del Día 11:

- **`CategoriaService.eliminarConTareas`** es el ejemplo real de
  `@Transactional`: desvincula todas las tareas de una categoría
  (`categoria = null`) y RECIÉN DESPUÉS la elimina, como una sola
  unidad atómica. Si algo fallara en el medio, ambos pasos se revierten
  juntos (rollback). Se agregó `TareaRepository.findByCategoriaId` para
  poder ubicar esas tareas.
  - Nota de arquitectura: esta operación inyecta `TareaRepository`
    directamente en `CategoriaService` (en vez de pasar por
    `TareaService`), para evitar una dependencia circular entre ambos
    Services. Es una excepción documentada a la regla general, no un
    descuido — ver el comentario en `CategoriaService`.
- **`/admin`** ahora tiene funcionalidad real: lista las categorías y
  permite eliminarlas (con confirmación), usando la operación
  transaccional de arriba.
- **La API REST queda protegida.** `SecurityConfig` se separó en DOS
  `SecurityFilterChain`:
  - `apiFilterChain` (`/api/**`): HTTP Basic (usuario/contraseña en el
    header `Authorization` de cada petición) + sesión `STATELESS` + sin
    CSRF (no aplica a un cliente sin cookies de sesión).
  - `webFilterChain` (todo lo demás): la misma configuración de los
    Días 9-10, sin cambios.
- **Notas de seguridad documentadas en el código** (sin cambios
  funcionales, porque ya eran seguros por diseño):
  - Inyección SQL: `JpaRepository` y los query methods SIEMPRE generan
    consultas parametrizadas — ver el comentario en `TareaRepository`.
  - XSS: `th:text` escapa automáticamente el contenido; `th:utext` no
    — ver el comentario en `tareas.html`.

## Cómo ejecutar

```bash
mvn spring-boot:run
```

- `http://localhost:8080/tareas` → sigue igual (login con formulario, como antes).
- `http://localhost:8080/api/tareas` → ahora pide autenticación. En Postman: pestaña **Authorization → Basic Auth**, usuario `estudiante@tallerpw.com`, contraseña `estudiante1234` (o `admin@tallerpw.com` / `admin1234`).
- `http://localhost:8080/admin` → logueado como ADMIN, ahora podés eliminar categorías desde la vista.

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
(ej. `Sprint 3: protege la API con HTTP Basic y agrega @Transactional`).

## HTTPS (nota conceptual, sin cambios de código)

En desarrollo local no configuramos HTTPS: alcanza con `http://localhost`.
En un despliegue real, HTTPS es indispensable (protege las contraseñas y
tokens viajando por la red). Dos formas típicas de habilitarlo:

- Configurar un certificado directamente en Spring Boot
  (`server.ssl.key-store`, `server.ssl.key-store-password`, etc. en
  `application.properties`).
- Delegarlo a un proxy inverso delante de la aplicación (Nginx, un
  balanceador de carga del proveedor de hosting) — el enfoque más común
  en producción, y el que se usa en el Día 14 (despliegue).

## Product Backlog (actualizado — Sprint 3, Día 12)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-09 | Endpoint REST de tareas | Alta | Sprint 3 | Hecho (Día 11) |
| HU-10 | Seguridad transaccional (incluye proteger la API) | Alta | Sprint 3 | **Hecho (Día 12)** |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | Backlog (Día 14) |

## Tablero Scrum/Kanban

Mover HU-10 a "Hecho". Preparar HU-11 (empaquetado y despliegue) para el Día 14.

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 13:** Calidad, manejo de errores y pruebas — `@ControllerAdvice`,
  `@ExceptionHandler`, logging con SLF4J/Logback, JUnit 5 y Mockito,
  documentación con Swagger/OpenAPI (ya instalado desde el Día 1).

