# Stage 1: Build the Spring Boot application
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the jar (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Install bash for sourcing .env
RUN apk add --no-cache bash

# Expose the port from .env
EXPOSE 1111

# Run the Spring Boot app with .env variables
ENTRYPOINT ["bash", "-c", "set -a && [ -f /app/.env ] && source /app/.env; exec java -jar app.jar"]
