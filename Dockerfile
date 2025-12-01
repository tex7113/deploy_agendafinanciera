# Stage 1: Build
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

# Copiamos solo el pom para cachear dependencias
COPY pom.xml .
RUN mvn -q dependency:go-offline

# Copiamos el código fuente
COPY src src

# Forzar UTF-8 para evitar MalformedInputException
RUN mvn -Dfile.encoding=UTF-8 -q package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copiar el JAR construido
COPY --from=build /workspace/target/agendafinanciera-0.0.1.jar app.jar

# Puerto de la aplicación
EXPOSE 8080

# Forzar UTF-8 en tiempo de ejecución también
ENTRYPOINT ["java", "-Dfile.encoding=UTF-8", "-jar", "app.jar"]