# Use official OpenJDK 17
FROM openjdk:17-jdk-slim AS build

WORKDIR /app

# Copy project files
COPY . .

# Build using Maven Wrapper
RUN ./mvnw -B -DskipTests package

# ---- Run stage ----
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Start the app
ENTRYPOINT ["java", "-jar", "app.jar"]
