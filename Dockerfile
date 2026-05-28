# ETAPA 1: Compilación (Maven 3.9.14 + JDK 21)
FROM maven:3.9.14-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copiamos el pom.xml para descargar las dependencias primero
# Esto permite que Docker guarde las librerías en caché si el pom no cambia
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copiamos el código fuente y generamos el JAR
COPY src ./src
RUN mvn clean package -DskipTests

# ETAPA 2: Ejecución (JRE 25 sobre Alpine 3.21)
FROM eclipse-temurin:25-jre-alpine-3.21
WORKDIR /app

# Copiamos el ejecutable desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto de Spring Boot
EXPOSE 8080

# Ejecutamos la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]