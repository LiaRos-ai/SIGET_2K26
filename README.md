# Sistema de Gestión de Tareas — Proyecto guía del curso

Proyecto**guia** que se desarrolla en vivo, sesión a sesión, durante las 3 semanas
del curso *Programación Web II*. Sirve como espejo técnico de lo que cada estudiante
debe ir aplicando en su propio proyecto (ver los 5 proyectos propuestos).

## Estado actual: Sprint 3 (Día 14) — Empaquetado y despliegue

Novedades del Día 14 respecto del Día 13:

- **`application.properties` se dividió en perfiles**:
  - `application.properties`: solo lo que es igual en cualquier entorno
    (puerto, Thymeleaf, patrón de logging) + `spring.profiles.active=dev`.
  - `application-dev.properties`: conexión a MySQL local, `ddl-auto=update`,
    `show-sql=true`, logging detallado — para programar cómodo.
  - `application-prod.properties`: credenciales desde variables de entorno
    (`${DB_URL}`, `${DB_USERNAME}`, `${DB_PASSWORD}`), `ddl-auto=validate`
    (Hibernate nunca modifica el esquema solo en producción), `show-sql=false`,
    logging más silencioso.
- **`Dockerfile`** (build multi-etapa): compila con Maven en una imagen,
  y copia solo el `.jar` resultante a una imagen final liviana con JRE
  (sin Maven ni código fuente en la imagen que se despliega).
- **`docker-compose.yml`**: levanta la aplicación Y una base de datos
  MySQL juntas, ya conectadas, con una sola línea (`docker compose up`).
- **`.dockerignore`**: evita copiar `target/`, `.git/`, logs, etc. dentro
  de la imagen — build más rápido y liviano.

## Cómo ejecutar (desarrollo local, sin Docker)

```bash
mvn spring-boot:run
```

Usa el perfil `dev` por defecto — sin cambios respecto a los días anteriores.

## Cómo empaquetar como JAR ejecutable

```bash
mvn clean package
java -jar target/gestion-tareas.jar
```

Para correrlo con el perfil de producción (necesita las variables de entorno
`DB_URL`, `DB_USERNAME`, `DB_PASSWORD` ya definidas):

```bash
SPRING_PROFILES_ACTIVE=prod java -jar target/gestion-tareas.jar
```

## Cómo desplegar con Docker

```bash
docker compose up --build
```

Esto levanta MySQL y la aplicación juntos. La app queda en `http://localhost:8080`,
usando el perfil `prod` (definido en `docker-compose.yml`).

Para construir solo la imagen de la aplicación (sin docker-compose, con una
base de datos externa ya existente):

```bash
docker build -t gestion-tareas .
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_URL=jdbc:mysql://mi-servidor:3306/gestion_tareas \
  -e DB_USERNAME=mi_usuario \
  -e DB_PASSWORD=mi_clave \
  gestion-tareas
```

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
(ej. `Sprint 3: agrega perfiles de configuración y despliegue con Docker`).

## Preparación de la sustentación final (actividad del Día 14)

El Día 15 cada equipo presenta su proyecto propio. Antes de esa clase, cada
equipo debería tener listo (ver también el documento "Checklist de
Sustentación Final"):

- El proyecto empaquetado (`mvn package`) sin errores.
- Una cuenta de prueba de cada rol (USER y ADMIN) para mostrar en vivo.
- Los 5 endpoints REST de su entidad principal, probados en Postman.
- El repositorio Git actualizado, con un README propio.
- Un recorrido de 5-7 minutos planeado: qué se muestra, en qué orden, quién
  habla en el equipo.

## Product Backlog (actualizado — Sprint 3, Día 14)

| # | Historia de usuario | Prioridad | Sprint estimado | Estado |
|---|---|---|---|---|
| HU-09 | Endpoint REST de tareas | Alta | Sprint 3 | Hecho (Día 11) |
| HU-10 | Seguridad transaccional | Alta | Sprint 3 | Hecho (Día 12) |
| HU-11 | Empaquetado y despliegue (JAR) | Alta | Sprint 3 | **Hecho (Día 14)** |

**Sprint 3 casi cerrado.** Falta la integración final y la sustentación
del Día 15.

## Tablero Scrum/Kanban

Mover HU-11 a "Hecho". El tablero debería estar prácticamente vacío de
pendientes antes del Día 15 (Integración final).

## Roles Scrum del curso

- **Product Owner / Scrum Master:** el docente.
- **Equipo de desarrollo:** los estudiantes, trabajando en el proyecto guía y en sus proyectos propios.

## Próximos hitos

- **Día 15:** Integración final de todos los módulos (Thymeleaf + REST +
  seguridad + BD), Sprint Review y Retrospectiva final, Evaluación
  semanal 3 / evaluación de promoción, sustentación de los 5 proyectos
  de estudiantes.
