from flask import Flask, request, jsonify
import proveedores
import pybreaker
from pybreaker import CircuitBreakerError
import os
import random
from tenacity import retry, stop_after_attempt, wait_fixed
import productos  
import comprasProveedores
from comprasProveedores import *
from productos import *
from proveedores import *
from flask_cors import CORS


app = Flask(__name__)
CORS(app)
PORT = int(os.environ.get("PORT", random.randint(5001, 5999)))

proveedores.registrar_en_eureka(PORT)
breaker = pybreaker.CircuitBreaker(fail_max=3, reset_timeout=10)
@app.route('/health')
def health():
    """Ruta de salud para verificar si el servicio está activo."""
    return "OK", 200

# Ruta para crear un producto
@app.route('/productos', methods=['POST'])
def crear_producto():
    data = request.json
    try:
        nombre = data.get('nombre')
        descripcion = data.get('descripcion')
        precio = data.get('precio')
        tipo_insumo = data.get('tipo_insumo')
        duracion_insumo = data.get('duracion_insumo')
        productos.crear_producto(nombre, descripcion, precio, tipo_insumo, duracion_insumo)
        return jsonify({"mensaje": "Producto creado exitosamente"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400

# Ruta para obtener todos los productos
@app.route('/productos', methods=['GET'])
def obtener_productos():
    try:
        productos_list = productos.obtener_productos()
        return jsonify(productos_list), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500



# Ruta para obtener un producto por ID con Circuit Breaker
@app.route('/productos/<int:producto_id>', methods=['GET'])
@breaker
def obtener_producto_por_id(producto_id):
    try:
        producto = productos.obtener_producto_por_id(producto_id)
        if producto:
            return jsonify(producto), 200
        else:
            return jsonify({"mensaje": "Producto no encontrado"}), 404
    except CircuitBreakerError:
        return jsonify({"error": "El servicio no está disponible temporalmente. Intente más tarde."}), 503
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/productos/<int:producto_id>', methods=['PUT'])
def editar_producto(producto_id):
    data = request.json
    try:
        nombre = data.get('nombre')
        descripcion = data.get('descripcion')
        precio = data.get('precio')
        tipo_insumo = data.get('tipo_insumo')
        duracion_insumo = data.get('duracion_insumo')
        productos.editar_producto(producto_id, nombre, descripcion, precio, tipo_insumo, duracion_insumo)
        return jsonify({"mensaje": "Producto actualizado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400

# Ruta para eliminar un producto por ID
@app.route('/productos/<int:producto_id>', methods=['DELETE'])
def eliminar_producto(producto_id):
    try:
        productos.eliminar_producto_por_id(producto_id)
        return jsonify({"mensaje": "Producto eliminado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/productos/nombre/<string:nombre_producto>', methods=['GET'])
def ruta_obtener_producto_por_nombre(nombre_producto):
    try:
        producto = obtener_producto_por_nombre(nombre_producto)
        if producto:
            return jsonify(producto), 200
        else:
            return jsonify({"error": "Producto no encontrado"}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    

@app.route('/productos/tipo/<string:tipo_insumo>', methods=['GET'])
def ruta_obtener_productos_por_tipo_insumo(tipo_insumo):
    try:
        productos = obtener_productos_por_tipo_insumo(tipo_insumo)
        if productos:
            return jsonify(productos), 200
        else:
            return jsonify({"error": "No se encontraron productos para el tipo de insumo especificado."}), 404
    except ValueError as e:
        print(f"Error de validación: {e}")
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        print(f"Error inesperado: {e}")
        return jsonify({"error": "Error interno del servidor"}), 500


@app.route('/productos/precio', methods=['GET'])
def ruta_obtener_productos_por_rango_precio():
    try:
        precio_min = request.args.get('precio_min', type=float)
        precio_max = request.args.get('precio_max', type=float)

        if precio_min is None or precio_max is None:
            return jsonify({"error": "Debe proporcionar los parámetros 'precio_min' y 'precio_max'."}), 400

        productos = obtener_productos_por_rango_precio(precio_min, precio_max)
        if productos:
            return jsonify(productos), 200
        else:
            return jsonify({"error": "No se encontraron productos en el rango de precios especificado."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500





# Ruta para crear un proveedor
@app.route('/proveedores', methods=['POST'])
def crear_proveedor():
    data = request.json
    try:
        nombre = data.get('nombre')
        telefono = data.get('telefono')
        direccion = data.get('direccion')
        email = data.get('email')
        ruc = data.get('ruc')  
        estado = data.get('estado', 'activo')  

        proveedores.crear_proveedor(nombre, telefono, direccion, email, ruc, estado)
        return jsonify({"mensaje": "Proveedor creado exitosamente"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/proveedores', methods=['GET'])
def obtener_proveedores():
    try:
        proveedores_list = proveedores.obtener_proveedores()
        return jsonify(proveedores_list), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/proveedores/<int:proveedor_id>', methods=['GET'])
@breaker
def obtener_proveedor_por_id(proveedor_id):
    try:
        proveedor = proveedores.obtener_proveedor_por_id(proveedor_id)
        if proveedor:
            return jsonify(proveedor), 200
        else:
            return jsonify({"mensaje": "Proveedor no encontrado"}), 404
    except CircuitBreakerError:
        return jsonify({"error": "El servicio no está disponible temporalmente. Intente más tarde."}), 503
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/proveedores/<int:proveedor_id>', methods=['PUT'])
def editar_proveedor(proveedor_id):
    data = request.json
    try:
        nombre = data.get('nombre')
        telefono = data.get('telefono')
        direccion = data.get('direccion')
        email = data.get('email')
        ruc = data.get('ruc')  
        estado = data.get('estado') 

        proveedores.editar_proveedor(proveedor_id, nombre, telefono, direccion, email, ruc, estado)
        return jsonify({"mensaje": "Proveedor actualizado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


# Ruta para eliminar un proveedor por ID
@app.route('/proveedores/<int:proveedor_id>', methods=['DELETE'])
def eliminar_proveedor(proveedor_id):
    try:
        proveedores.eliminar_proveedor_por_id(proveedor_id)
        return jsonify({"mensaje": "Proveedor eliminado exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500

@app.route('/proveedores/estado/<string:estado>', methods=['GET'])
def ruta_obtener_proveedores_por_estado(estado):
    try:
        proveedores = obtener_proveedores_por_estado(estado)
        if proveedores:
            return jsonify(proveedores), 200
        else:
            return jsonify({"error": "No se encontraron proveedores con el estado especificado."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500



# Ruta para crear una compra de proveedor
@app.route('/compras-proveedores', methods=['POST'])
def crear_compra_proveedor():
    data = request.json
    try:
        proveedor_id = data.get('proveedor_id')
        producto_id = data.get('producto_id')
        cantidad = data.get('cantidad')
        unidad_medida = data.get('unidad_medida')
        if not proveedor_id or not producto_id or not cantidad or not unidad_medida:
            raise ValueError("Todos los campos (proveedor_id, producto_id, cantidad, unidad_medida) son obligatorios.")
        compra_proveedor_id = comprasProveedores.crear_compra_proveedor(proveedor_id, producto_id, cantidad, unidad_medida)
        producto = productos.obtener_producto_por_id(producto_id)
        if not producto:
            raise ValueError("El producto con el ID proporcionado no existe.")
        tipo_insumo = producto.get('tipo_insumo')
        duracion_insumo = producto.get('duracion_insumo')
        if not tipo_insumo or not duracion_insumo:
            raise ValueError("El producto no tiene configurados los campos tipo_insumo o duracion_insumo.")
        resultado_bodega = crear_registro_en_bodega_desde_proveedor(compra_proveedor_id, tipo_insumo, duracion_insumo)
        if "error" in resultado_bodega:
            return jsonify({"mensaje": "Compra creada pero error al registrar en bodega", "detalle": resultado_bodega["error"]}), 500
        return jsonify({"mensaje": "Compra de proveedor creada y registrada en bodega exitosamente"}), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400

# Ruta para obtener todas las compras de proveedores
@app.route('/compras-proveedores', methods=['GET'])
def obtener_compras_proveedores():
    try:
        compras = comprasProveedores.obtener_compras_proveedores()
        return jsonify(compras), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


# Ruta para obtener una compra de proveedor por ID con Circuit Breaker
@app.route('/compras-proveedores/<int:compra_id>', methods=['GET'])
@breaker
def obtener_compra_proveedor_por_id(compra_id):
    try:
        compra = comprasProveedores.obtener_compra_proveedor_por_id(compra_id)
        if compra:
            return jsonify(compra), 200
        else:
            return jsonify({"mensaje": "Compra de proveedor no encontrada"}), 404
    except CircuitBreakerError:
        return jsonify({"error": "El servicio no está disponible temporalmente. Intente más tarde."}), 503
    except Exception as e:
        return jsonify({"error": str(e)}), 500


# Ruta para editar una compra de proveedor
@app.route('/compras-proveedores/<int:compra_id>', methods=['PUT'])
def editar_compra_proveedor(compra_id):
    data = request.json
    try:
        proveedor_id = data.get('proveedor_id')
        producto_id = data.get('producto_id')
        cantidad = data.get('cantidad')
        unidad_medida = data.get('unidad_medida')

        if not cantidad and not unidad_medida and not proveedor_id and not producto_id:
            raise ValueError("Debe proporcionar al menos un campo para actualizar.")

        comprasProveedores.editar_compra_proveedor(compra_id, proveedor_id, producto_id, cantidad, unidad_medida)
        return jsonify({"mensaje": "Compra de proveedor actualizada exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


# Ruta para eliminar una compra de proveedor por ID
@app.route('/compras-proveedores/<int:compra_id>', methods=['DELETE'])
def eliminar_compra_proveedor(compra_id):
    try:
        comprasProveedores.eliminar_compra_proveedor_por_id(compra_id)
        return jsonify({"mensaje": "Compra de proveedor eliminada exitosamente"}), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    
@app.route('/compras/fecha', methods=['GET'])
def ruta_obtener_compras_por_rango_fecha():
    try:
        fecha_inicio = request.args.get('fecha_inicio')
        fecha_fin = request.args.get('fecha_fin')

        if not fecha_inicio or not fecha_fin:
            return jsonify({"error": "Debe proporcionar los parámetros 'fecha_inicio' y 'fecha_fin'."}), 400

        compras = obtener_compras_por_rango_fecha(fecha_inicio, fecha_fin)
        if compras:
            return jsonify(compras), 200
        else:
            return jsonify({"error": "No se encontraron compras en el rango de fechas especificado."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    

@app.route('/compras/fecha-especifica', methods=['GET'])
def ruta_obtener_compras_por_fecha():
    try:
        fecha = request.args.get('fecha')

        if not fecha:
            return jsonify({"error": "Debe proporcionar el parámetro 'fecha'."}), 400

        compras = obtener_compras_por_fecha(fecha)
        if compras:
            return jsonify(compras), 200
        else:
            return jsonify({"error": "No se encontraron compras en la fecha especificada."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500


if __name__ == "__main__":
    app.run(port=PORT, debug=False)