# Use Java 22 JDK runtime
FROM eclipse-temurin:22-jdk

# Set working directory inside container
WORKDIR /app

# Copy your JAR
COPY target/stock-advisor-0.0.1-SNAPSHOT.jar app.jar

# Expose the port the Spring Boot app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
