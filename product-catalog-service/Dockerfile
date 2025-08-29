# Use uma imagem base com JDK 21
FROM openjdk:21-jdk

WORKDIR /app

COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml
COPY ./.mvn ./.mvn

# Copie o código-fonte
COPY ./src ./src

# Instale o Maven e compile a aplicação
RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/product-catalog-service-0.0.1-SNAPSHOT.jar"]