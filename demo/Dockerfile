# Use an official OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code first
COPY . .

# Grant execute permission to the Maven wrapper
RUN chmod +x ./mvnw

# Build the app and skip tests to save time
RUN ./mvnw clean package -DskipTests

# Find the built jar file and copy it to a standard name
# This line finds the jar regardless of its exact version name
RUN cp target/*.jar app.jar

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]