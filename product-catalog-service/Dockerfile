# --- Estágio 1: Builder com GraalVM ---
# Usamos uma imagem que já vem com o JDK e as ferramentas de build nativo da GraalVM
FROM container-registry.oracle.com/graalvm/native-image:21 AS builder

WORKDIR /app

# Copia os ficheiros de build primeiro para aproveitar o cache de layers do Docker
COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml

# Descarrega as dependências
RUN ./mvnw dependency:go-offline

# Copia o código fonte
COPY ./src ./src

# Executa o build nativo ativando o perfil 'native'
RUN ./mvnw clean package -Pnative -DskipTests


# --- Estágio 2: Imagem Final ---
# Usamos uma imagem base slim do Debian 12
FROM debian:12-slim

# Instala a biblioteca 'zlib' (que fornece o libz.so.1) e o ca-certificates (para conexões HTTPS)
RUN apt-get update && apt-get install -y libz-dev ca-certificates && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copia o executável do estágio de builder
COPY --from=builder /app/target/product-catalog-service .

# Expõe a porta
EXPOSE 8083

# Comando para iniciar a aplicação
CMD ["/app/product-catalog-service"]