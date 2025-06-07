from flask import Flask, request, jsonify
import proveedores
import pybreaker
from pybreaker import CircuitBreakerError
import os
import random
from tenacity import retry, stop_after_attempt, wait_fixed
import productos  
import comprasProveedores
from comprasProveedores import crear_registro_en_bodega_desde_proveedor
app = Flask(__name__)
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
        productos.crear_producto(nombre, descripcion, precio)
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


# Ruta para editar un producto
@app.route('/productos/<int:producto_id>', methods=['PUT'])
def editar_producto(producto_id):
    data = request.json
    try:
        nombre = data.get('nombre')
        descripcion = data.get('descripcion')
        precio = data.get('precio')
        productos.editar_producto(producto_id, nombre, descripcion, precio)
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


# Ruta para obtener todos los proveedores
@app.route('/proveedores', methods=['GET'])
def obtener_proveedores():
    try:
        proveedores_list = proveedores.obtener_proveedores()
        return jsonify(proveedores_list), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


# Ruta para obtener un proveedor por ID con Circuit Breaker
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


# Ruta para editar un proveedor
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
        tipo_insumo = "EjemploTipo" 
        duracion_insumo = 30  
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

        # Validar los datos
        if not cantidad and not unidad_medida and not proveedor_id and not producto_id:
            raise ValueError("Debe proporcionar al menos un campo para actualizar.")

        # Llamar a la función para editar la compra
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
    

if __name__ == "__main__":
    app.run(port=PORT, debug=False)