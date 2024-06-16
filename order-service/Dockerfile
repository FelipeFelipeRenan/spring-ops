# Use a base image with JDK 21
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper files and the pom.xml
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml
COPY ./.mvn ./.mvn

# Copy the source code
COPY ./src ./src

# Install Maven and package the application
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Specify the command to run the application
CMD ["java", "-jar", "target/order-service-0.0.1-SNAPSHOT.jar"]
