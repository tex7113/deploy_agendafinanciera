FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -q dependency:go-offline

COPY src src

# Forzar UTF-8 para evitar MalformedInputException
RUN mvn -Dfile.encoding=UTF-8 -q package -DskipTests

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /workspace/target/agendafinanciera-0.0.1.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]