# Spring Commerce Ops

Bem-vindo ao **Spring Commerce Ops**, um projeto de referência concebido para ser um *benchmark* de uma plataforma de e-commerce moderna, construída com as mais recentes tecnologias e padrões do ecossistema Java.

O objetivo deste projeto é explorar e demonstrar a construção de um sistema de microserviços de alta performance, reativo, orientado a eventos e altamente observável, seguindo as melhores práticas de DevOps e engenharia de software.

## Visão Arquitetural

A arquitetura é projetada para ser elástica, resiliente e responsiva, abraçando os quatro pilares do [Manifesto Reativo](https://www.reactivemanifesto.org/).

### Padrões Fundamentais

* **Microserviços:** Cada serviço tem uma responsabilidade única e bem definida, representando um domínio de negócio específico.
* **Orientado a Eventos:** A comunicação entre os serviços críticos é primariamente assíncrona, utilizando o Apache Kafka como a espinha dorsal de eventos.
* **Programação Reativa:** A stack é reativa de ponta a ponta (end-to-end), desde o API Gateway até à base de dados, utilizando Spring WebFlux e R2DBC para maximizar a escalabilidade e a eficiência de recursos.
* **Compilação Nativa (AOT):** Os microserviços são compilados para executáveis nativos com GraalVM, resultando em tempos de arranque na ordem dos milissegundos e um consumo de memória drasticamente reduzido.
* **Persistência Poliglota:** Utilizamos a base de dados mais adequada para cada tarefa (PostgreSQL para dados relacionais, Redis para cache e operações atómicas).
* **DevOps & Observabilidade:** Infraestrutura como código (Docker Compose), pipelines de CI/CD e monitorização completa com Prometheus e Grafana.

### Serviços do Ecossistema

| Serviço | Porta | Descrição | Status |
| :--- | :--- | :--- | :--- |
| `service-discovery` | 8761 | Registo e descoberta de serviços (Netflix Eureka). | ✅ Implementado |
| `api-gateway` | 8080 | Ponto de entrada único, roteamento e segurança (Spring Cloud Gateway). | ✅ Implementado |
| `product-catalog-service` | 8083 | Gestão do catálogo de produtos (WebFlux, R2DBC, PostgreSQL, GraalVM). | ✅ Implementado |
| `inventory-service` | 8084 | Controlo de stock em tempo real (WebFlux, Redis, GraalVM). | 🚧 Em Desenvolvimento |
| `kafka` | 9092 | Broker de eventos para comunicação assíncrona. | ✅ Implementado |
| `kafka-ui` | 8989 | UI para visualizar tópicos e mensagens do Kafka. | ✅ Implementado |
| `postgres` | 5432 | Base de dados relacional. | ✅ Implementado |
| `redis` | 6379 | Base de dados em memória para cache e inventário. | ✅ Implementado |
| `prometheus` | 9090 | Coleta de métricas. | ✅ Implementado |
| `grafana` | 3000 | Dashboard para visualização de métricas. | ✅ Implementado |

## Stack Tecnológica

* **Backend:** Java 21, Spring Boot 3, Spring WebFlux, Spring Data R2DBC, Spring Cloud
* **Persistência:** PostgreSQL, Redis
* **Mensageria:** Apache Kafka
* **Build Nativo:** GraalVM
* **DevOps:** Docker, Docker Compose, Jenkinsfile, GitHub Actions
* **Observabilidade:** Micrometer, Prometheus, Grafana
* **Documentação de API:** springdoc-openapi (Swagger)

## Como Executar o Projeto

### Pré-requisitos

* Docker e Docker Compose
* JDK 21+ (para a sua IDE)
* Maven

O projeto está configurado para dois ambientes distintos, utilizando um `Makefile` para simplificar os comandos.

### Ambiente de Desenvolvimento (JVM - Rápido)

Ideal para o desenvolvimento diário. Constrói e executa os serviços na JVM.

```bash
# Constrói e inicia todos os contentores em modo de desenvolvimento
make start-dev
```

### Ambiente Nativo (Lento para construir, Rápido para executar)

Constrói os serviços compatíveis como executáveis nativos GraalVM.

```bash
# Constrói (sem cache) e inicia todos os contentores em modo nativo
make start-native
```

### Comandos Úteis (via Makefile)

```bash
# Para e remove todos os contentores e volumes (limpeza total)
make clean

# Mostra os logs de todos os serviços em tempo real
make logs

# Lista todos os comandos disponíveis
make help
```

### Endpoints importantes

* **API Gateway:** http://localhost:8080
* **Swagger UI (Product Catalog):** http://localhost:8080/swagger-ui.html
* **Eureka Discovery Server:** http://localhost:8761
* **Kafka UI:** http://localhost:88989
* **Prometheus:** http://localhost:9090
* **Grafana:** http://localhost:3000 (user/pass: admin/admin)
