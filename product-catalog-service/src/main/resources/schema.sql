-- Remove a tabela antiga se existir, para garantir um início limpo
DROP TABLE IF EXISTS products;

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    sku VARCHAR(100) UNIQUE NOT NULL, -- Stock Keeping Unit, um identificador único de negócio
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(19, 4) NOT NULL, -- Maior precisão para valores financeiros
    brand VARCHAR(100),
    category VARCHAR(100),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
    version INT NOT NULL -- Para controle de concorrência (Optimistic Locking)
);