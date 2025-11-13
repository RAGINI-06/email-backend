

# Use Eclipse Temurin JDK 21 (LTS) slim image
FROM eclipse-temurin:21-jdk-jammy

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

# Expose port
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "target/email-writer-sb-0.0.1-SNAPSHOT.jar"]
