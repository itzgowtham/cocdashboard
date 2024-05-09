# Use a base image with Java installed
FROM openjdk:17-alpine
 
# Set the working directory in the container
WORKDIR /app
 
# Copy the packaged JAR file into the container at /app
COPY target/CoCDashboard-backend-0.0.1-SNAPSHOT.jar /app/application.jar
 
# Expose the port that your application runs on
EXPOSE 8080
 
# Run the JAR file when the container launches
CMD ["java", "-jar", "--add-opens=java.base/java.nio=ALL-UNNAMED", "application.jar"]
