# Use a imagem base com JDK 21
FROM openjdk:21-jdk

# Set the working directory in the container
WORKDIR /app

# Copie o Maven wrapper e o arquivo pom.xml
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml
COPY ./.mvn ./.mvn

# Copie o código-fonte
COPY ./src ./src

# Instale o Maven e compile a aplicação
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

# Especifique o comando para iniciar o Eureka Server
CMD ["java", "-jar", "target/api-gateway-0.0.1-SNAPSHOT.jar"]
