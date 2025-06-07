-- Crear bases de datos primero
CREATE DATABASE proveedor_db;
CREATE DATABASE inventario_db;
CREATE DATABASE pedido_db;
CREATE DATABASE cliente_db;
CREATE DATABASE venta_db;
CREATE DATABASE reportes_db;

-- Proveedor DB
USE proveedor_db;
CREATE TABLE proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    email VARCHAR(100) UNIQUE
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    precio DECIMAL(10,2) CHECK (precio >= 0)
);

CREATE TABLE compra_proveedor (
    id INT AUTO_INCREMENT PRIMARY KEY,
    proveedor_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT CHECK (cantidad > 0),
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);

CREATE INDEX idx_proveedor_email ON proveedor(email);

-- Inventario DB
USE inventario_db;
CREATE TABLE bodega (
    id INT AUTO_INCREMENT PRIMARY KEY,
    compra_proveedor_id INT NOT NULL,
    fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cantidad INT CHECK (cantidad > 0),
    unidad_medida VARCHAR(50),
    tipo_insumo VARCHAR(255),
    duracion_insumo VARCHAR(255),
    FOREIGN KEY (compra_proveedor_id) REFERENCES proveedor_db.compra_proveedor(id)
);

CREATE TABLE inventario_cocina (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bodega_id INT NOT NULL,
    cantidad_disponible DECIMAL(10,2),
    ultima_fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bodega_id) REFERENCES bodega(id)
);

CREATE TABLE inventario_barra (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bodega_id INT NOT NULL,
    cantidad_disponible DECIMAL(10,2),
    fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (bodega_id) REFERENCES bodega(id)
);

CREATE INDEX idx_bodega_tipo_insumo ON bodega(tipo_insumo);

-- Cliente DB
USE cliente_db;
CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100),
    dni VARCHAR(15) UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100) UNIQUE,
    direccion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE administrador (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cliente_dni ON cliente(dni);

-- Pedido DB
USE pedido_db;
CREATE TABLE receta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    cantidad INT CHECK (cantidad > 0),
    unidad_medida VARCHAR(50)
);

CREATE TABLE menu (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DECIMAL(10,2),
    tipo VARCHAR(100),
    imagen TEXT,
    receta_id INT NOT NULL,
    FOREIGN KEY (receta_id) REFERENCES receta(id)
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    cliente_id INT NOT NULL,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_pedido ENUM('pendiente', 'en preparación', 'completado', 'cancelado') DEFAULT 'pendiente',
    FOREIGN KEY (cliente_id) REFERENCES cliente_db.cliente(id)
);

CREATE TABLE detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    menu_id INT NOT NULL,
    cantidad INT CHECK (cantidad > 0),
    precio_unitario DECIMAL(10,2),
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (menu_id) REFERENCES menu(id)
);

CREATE INDEX idx_pedido_estado ON pedido(estado_pedido);

-- Venta DB
USE venta_db;
CREATE TABLE venta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2),
    metodo_pago ENUM('efectivo', 'tarjeta', 'transferencia') NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido_db.pedido(id)
);

CREATE TABLE promociones (
    id INT AUTO_INCREMENT PRIMARY KEY,
    valor_descuento DECIMAL(10,2),
    motivo VARCHAR(100)
);

CREATE TABLE comprobante_pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    venta_id INT NOT NULL,
    tipo ENUM('boleta', 'factura') NOT NULL,
    numeroSerie VARCHAR(10) NOT NULL,
    numeroComprobante VARCHAR(15) NOT NULL,
    fechaEmision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    igv DECIMAL(10,2),
    montoNeto DECIMAL(10,2),
    FOREIGN KEY (venta_id) REFERENCES venta(id)
);

CREATE INDEX idx_venta_metodo_pago ON venta(metodo_pago);

-- Reportes DB
USE reportes_db;
CREATE TABLE reportes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    administrador_id INT NULL,
    venta_id INT NULL,
    bodega_id INT NULL,
    cliente_id INT NULL,
    inventario_cocina_id INT NULL,
    inventario_barra_id INT NULL,
    detalle_pedido_id INT NULL,
    descripcion VARCHAR(255) NOT NULL, 
    detalles TEXT NULL,
    tipo ENUM('ventas', 'clientes', 'inventario', 'administración') NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (administrador_id) REFERENCES cliente_db.administrador(id),
    FOREIGN KEY (venta_id) REFERENCES venta_db.venta(id),
    FOREIGN KEY (bodega_id) REFERENCES inventario_db.bodega(id),
    FOREIGN KEY (cliente_id) REFERENCES cliente_db.cliente(id),
    FOREIGN KEY (inventario_cocina_id) REFERENCES inventario_db.inventario_cocina(id),
    FOREIGN KEY (inventario_barra_id) REFERENCES inventario_db.inventario_barra(id),
    FOREIGN KEY (detalle_pedido_id) REFERENCES pedido_db.detalle_pedido(id)
);
