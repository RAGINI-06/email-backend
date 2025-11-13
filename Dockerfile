
# Use the latest Java 21 slim image
FROM openjdk:21-jdk-slim

# Set working directory inside the container
WORKDIR /app

# Copy Maven wrapper and pom.xml first (for dependency caching)
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Give permission to mvnw (fixes “Permission denied” issue)
RUN chmod +x mvnw

# Download dependencies (to speed up builds)
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the project without running tests
RUN ./mvnw clean package -DskipTests

# Expose port (change if your app uses a different one)
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "target/email-writer-sb-0.0.1-SNAPSHOT.jar"]
