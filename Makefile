# Makefile para o projeto Spring Commerce Ops

# Define os ficheiros compose a serem usados para cada ambiente
DEV_COMPOSE_FILE := docker-compose.yml
NATIVE_COMPOSE_FILE := docker-compose.native.yml

.PHONY: help start-dev start-native stop clean logs-dev logs-native ps-dev ps-native build-dev build-native

help: ## Mostra esta ajuda
	@echo "Comandos disponíveis para o projeto Spring Commerce Ops:"
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

start-dev: ## Constrói e inicia o ambiente de desenvolvimento (JVM)
	@echo "🚀 Iniciando o ambiente de desenvolvimento (JVM)..."
	docker-compose -f $(DEV_COMPOSE_FILE) up --build -d

start-native: ## Constrói (sem cache) e inicia o ambiente nativo (GraalVM)
	@echo "🚀 Iniciando o build nativo (Pode demorar vários minutos)..."
	docker-compose -f $(NATIVE_COMPOSE_FILE) up --build --force-recreate -d

stop: ## Para os containers de AMBOS os ambientes
	@echo "🛑 Parando todos os ambientes..."
	docker-compose -f $(DEV_COMPOSE_FILE) stop
	docker-compose -f $(NATIVE_COMPOSE_FILE) stop

clean: ## Para e remove todos os containers, redes e volumes
	@echo "🧹 Limpando completamente o ambiente..."
	docker-compose -f $(DEV_COMPOSE_FILE) down -v
	docker-compose -f $(NATIVE_COMPOSE_FILE) down -v

logs-dev: ## Mostra os logs do ambiente de desenvolvimento (JVM)
	@echo "📜 Mostrando os logs do ambiente DEV..."
	docker-compose -f $(DEV_COMPOSE_FILE) logs -f

logs-native: ## Mostra os logs do ambiente nativo (GraalVM)
	@echo "📜 Mostrando os logs do ambiente NATIVO..."
	docker-compose -f $(NATIVE_COMPOSE_FILE) logs -f

ps-dev: ## Lista os containers do ambiente de desenvolvimento (JVM)
	@docker-compose -f $(DEV_COMPOSE_FILE) ps

ps-native: ## Lista os containers do ambiente nativo (GraalVM)
	@docker-compose -f $(NATIVE_COMPOSE_FILE) ps

build-dev: ## Constrói as imagens de desenvolvimento (JVM)
	@echo "🛠️ Construindo as imagens de desenvolvimento (JVM)..."
	docker-compose -f $(DEV_COMPOSE_FILE) build

build-native: ## Constrói as imagens nativas (GraalVM) sem cache
	@echo "🛠️ Construindo as imagens nativas (GraalVM)... Este processo é demorado."
	docker-compose -f $(NATIVE_COMPOSE_FILE) build --no-cache