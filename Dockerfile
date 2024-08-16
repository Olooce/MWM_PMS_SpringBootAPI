# Use the official Maven image to compile and build your app
# with OpenJDK 17 as the base image
FROM maven:3.8.5-openjdk-17-slim AS build

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml file and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project into the container //
COPY . .

# Build the project and package it into a JAR file
RUN mvn clean package -DskipTests

# Use the official OpenJDK 17 runtime image for running the app
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the application runs on
EXPOSE 8080

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
