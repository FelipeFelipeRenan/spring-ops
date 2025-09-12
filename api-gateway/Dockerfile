# --- Estágio 1: Builder com Maven e JDK ---
FROM openjdk:21-jdk AS builder
WORKDIR /app
COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml
RUN ./mvnw dependency:go-offline
COPY ./src ./src
RUN ./mvnw clean package -DskipTests

# --- Estágio 2: Imagem Final ---
# Usamos a imagem oficial e moderna do Eclipse Temurin 21 JRE, que é leve
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o ficheiro JAR do estágio de builder
COPY --from=builder /app/target/api-gateway-0.0.1-SNAPSHOT.jar .

EXPOSE 8080
# Comando para iniciar a aplicação via JVM
CMD ["java", "-jar", "api-gateway-0.0.1-SNAPSHOT.jar"]