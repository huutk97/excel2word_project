# --- Build stage ---
FROM maven:3.8.7-eclipse-temurin-8 AS builder
WORKDIR /app

# Copy toàn bộ source
COPY . .

# Build bằng Maven có sẵn trong image
RUN mvn clean package -DskipTests

# --- Run stage ---
FROM eclipse-temurin:8-jre
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
