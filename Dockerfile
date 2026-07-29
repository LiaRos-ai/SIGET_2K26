# =========================================================
# Dockerfile — Día 14
# =========================================================
# Build multi-etapa: la etapa 1 compila el proyecto con Maven (necesita
# el JDK completo + Maven); la etapa 2 es la imagen que realmente se
# despliega, y NO tiene ni Maven ni el código fuente — solo el .jar ya
# compilado y un JRE (Java Runtime Environment, más liviano que el JDK
# completo). Resultado: una imagen final mucho más chica y sin
# herramientas de build innecesarias en producción.

# ---------- Etapa 1: build ----------
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiar primero solo el pom.xml permite que Docker reutilice (cachee)
# las dependencias descargadas si el código cambia pero el pom.xml no.
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ---------- Etapa 2: imagen final ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# El nombre del .jar generado coincide con <finalName> en el pom.xml.
COPY --from=build /app/target/gestion-tareas.jar app.jar

EXPOSE 8080

# SPRING_PROFILES_ACTIVE se define normalmente en docker-compose.yml o al
# correr "docker run", no acá — así la misma imagen sirve para distintos
# entornos sin reconstruirla.
ENTRYPOINT ["java", "-jar", "app.jar"]
