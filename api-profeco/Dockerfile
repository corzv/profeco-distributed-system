# Compilar
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# Pom
COPY pom.xml .
RUN mvn -B dependency:go-offline

# Codigo
COPY src ./src
RUN mvn -B package -DskipTests

# Ejecucion
FROM eclipse-temurin:21-jre
LABEL authors="corzv"
WORKDIR /app

# Quarkus
COPY --from=build /build/target/quarkus-app/ ./
USER 10001
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
