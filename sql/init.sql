-- SQLite no necesita CREATE DATABASE ni USE
-- Las tablas se crean directamente

CREATE TABLE IF NOT EXISTS ventas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    codigo VARCHAR(20) NOT NULL,
    fecha DATE NOT NULL,
    total DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS productos (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50),
    stock INT NOT NULL DEFAULT 0,
    precio DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS detalle_ventas (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    id_venta INT NOT NULL,
    producto_codigo VARCHAR(20) NOT NULL,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id),
    FOREIGN KEY (producto_codigo) REFERENCES productos(codigo)
);

-- Datos iniciales
INSERT OR IGNORE INTO productos (codigo, nombre, categoria, stock, precio) VALUES
('ARROZ001', 'Arroz Costeño', 'Alimentos', 50, 12.50),
('ACEITE001', 'Aceite Primor', 'Alimentos', 30, 18.00),
('AZUCAR001', 'Azúcar Rubia', 'Alimentos', 40, 8.50),
('LECHE001', 'Leche Gloria', 'Lácteos', 25, 6.50),
('PAN001', 'Pan de Molde', 'Panadería', 100, 2.50),
('JABON001', 'Jabón Bolivar', 'Limpieza', 60, 4.50),
('SHAMPOO001', 'Shampoo Sedal', 'Cuidado Personal', 20, 15.00),
('CEREAL001', 'Cereal Kellogg''s', 'Alimentos', 35, 12.00),
('ATUN001', 'Atún Florida', 'Conservas', 45, 7.50),
('GALLETA001', 'Galletas Casino', 'Alimentos', 80, 5.50);