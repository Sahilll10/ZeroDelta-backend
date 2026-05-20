# Use an official OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the project files
COPY . .

# Build the app using the Maven wrapper
RUN ./mvnw clean package -DskipTests

# Run the jar file
ENTRYPOINT ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]