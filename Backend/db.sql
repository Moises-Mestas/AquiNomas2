-- Base de datos proveedor_db
CREATE DATABASE proveedor_db;
USE proveedor_db;

CREATE TABLE proveedor (
    id INT PRIMARY KEY,
    nombre VARCHAR(100),
    telefono VARCHAR(20),
    direccion VARCHAR(255),
    email VARCHAR(100)
);

CREATE TABLE producto (
    id INT PRIMARY KEY,
    nombre VARCHAR(100),
    descripcion TEXT,
    precio DECIMAL(10,2)
);

CREATE TABLE compra_proveedor (
    id INT PRIMARY KEY,
    proveedor_id INT,
    producto_id INT,
    cantidad INT,
    FOREIGN KEY (proveedor_id) REFERENCES proveedor(id),
    FOREIGN KEY (producto_id) REFERENCES producto(id)
);


-- Base de datos insumo_db
CREATE DATABASE insumo_db;
USE insumo_db;

CREATE TABLE bodega (
    id INT PRIMARY KEY,
    compra_proveedor_id INT,
    fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cantidad INT,
    cantidad_minima INT,
    unidad_medida VARCHAR(100),
    tipo_insumo VARCHAR(255),
    duracion_insumo VARCHAR(255),
    FOREIGN KEY (compra_proveedor_id) REFERENCES proveedor_db.compra_proveedor(id)
);


-- Base de datos inventario_db
CREATE DATABASE inventario_db;
USE inventario_db;

CREATE TABLE inventario_cocina (
    id INT PRIMARY KEY,
    bodega_id INT,
    cantidad_disponible DECIMAL(10,2),
    ultima_fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ultima_fecha_salida TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cantidad_usada INT,
    FOREIGN KEY (bodega_id) REFERENCES insumo_db.bodega(id)
);

CREATE TABLE inventario_barra (
    id INT PRIMARY KEY,
    bodega_id INT,
    insumos_disponibles DECIMAL(10,3),
    fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_apertura TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    cantidad_usada INT,
    FOREIGN KEY (bodega_id) REFERENCES insumo_db.bodega(id)
);

CREATE TABLE receta (
    id INT PRIMARY KEY,
    inventario_cocina_id INT,
    inventario_barra_id INT,
    cantidad INT,
    unidad_medida VARCHAR(100),
    FOREIGN KEY (inventario_cocina_id) REFERENCES inventario_cocina(id),
    FOREIGN KEY (inventario_barra_id) REFERENCES inventario_barra(id)
);

CREATE TABLE menu (
    id INT PRIMARY KEY,
    nombre VARCHAR(100),
    recetas_id INT,
    descripcion VARCHAR(100),
    precio DECIMAL(10,2),
    tipo VARCHAR(100),
    imagen TEXT,
    FOREIGN KEY (recetas_id) REFERENCES receta(id)
);


-- Base de datos pedido_db
CREATE DATABASE pedido_db;
USE pedido_db;

CREATE TABLE pedido (
    id INT PRIMARY KEY,
    administrador_id INT,
    detalle_pedido_id INT,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado_pedido VARCHAR(50),
    FOREIGN KEY (administrador_id) REFERENCES cliente_db.administrador(id),
    FOREIGN KEY (detalle_pedido_id) REFERENCES detalle_pedido(id)
);

CREATE TABLE detalle_pedido (
    id INT PRIMARY KEY,
    cliente_id INT,
    menu_id INT,
    cantidad INT,
    precio_unitario DECIMAL(10,2),
    FOREIGN KEY (cliente_id) REFERENCES cliente_db.cliente(id),
    FOREIGN KEY (menu_id) REFERENCES inventario_db.menu(id)
);


-- Base de datos cliente_db
CREATE DATABASE cliente_db;
USE cliente_db;

CREATE TABLE cliente (
    id INT PRIMARY KEY,
    nombre VARCHAR(100),
    apellido VARCHAR(100),
    dni VARCHAR(15) UNIQUE,
    ruc VARCHAR(15) UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100),
    direccion VARCHAR(255),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE administrador (
    id INT PRIMARY KEY,
    nombre VARCHAR(100),
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2),
    estado VARCHAR(50)
);


-- Base de datos venta_db
CREATE DATABASE venta_db;
USE venta_db;

CREATE TABLE venta (
    id INT PRIMARY KEY,
    pedido_id INT,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    promociones_id INT,
    total DECIMAL(10,2),
    metodo_pago VARCHAR(100),
    FOREIGN KEY (pedido_id) REFERENCES pedido_db.pedido(id),
    FOREIGN KEY (promociones_id) REFERENCES promociones(id)
);

CREATE TABLE promociones (
    id INT PRIMARY KEY,
    valor_descuento DECIMAL(10,2),
    motivo VARCHAR(100)
);

CREATE TABLE comprobante_pago (
    id INT PRIMARY KEY,
    venta_id INT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    numeroSerie VARCHAR(10) NOT NULL,
    numeroComprobante VARCHAR(15) NOT NULL,
    fechaEmision TIMESTAMP NOT NULL,
    igv DECIMAL(10,2),
    montoNeto DECIMAL(10,2),
    FOREIGN KEY (venta_id) REFERENCES venta(id)
);


-- Base de datos reportes_db
CREATE DATABASE reportes_db;
USE reportes_db;

CREATE TABLE reportes (
    id INT PRIMARY KEY,
    administrador_id INT NULL,
    venta_id INT NULL,
    bodega_id INT NULL,
    cliente_id INT,
    inventario_cocina_id INT NULL,
    inventario_barra_id INT NULL,
    detalle_pedido_id INT NULL,
    descripcion VARCHAR(50) NOT NULL,
    tipo VARCHAR(100),
    FOREIGN KEY (administrador_id) REFERENCES cliente_db.administrador(id),
    FOREIGN KEY (venta_id) REFERENCES venta_db.venta(id),
    FOREIGN KEY (bodega_id) REFERENCES insumo_db.bodega(id),
    FOREIGN KEY (cliente_id) REFERENCES cliente_db.cliente(id),
    FOREIGN KEY (inventario_cocina_id) REFERENCES inventario_db.inventario_cocina(id),
    FOREIGN KEY (inventario_barra_id) REFERENCES inventario_db.inventario_barra(id),
    FOREIGN KEY (detalle_pedido_id) REFERENCES pedido_db.detalle_pedido(id)
);