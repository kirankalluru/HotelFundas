# Use OpenJDK 11 as the base image
FROM openjdk:11-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on (default 8080)

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]