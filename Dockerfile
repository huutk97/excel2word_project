# --- Build stage ---
FROM maven:3.8.7-eclipse-temurin-8 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn -e -B dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:8-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

ENV PORT=8080
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
