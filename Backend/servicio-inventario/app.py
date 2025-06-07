from flask import Flask, request, jsonify
import bodega
import pybreaker
import os
import random
import inventarioBarra
from tenacity import retry, stop_after_attempt, wait_fixed

app = Flask(__name__)
PORT = int(os.environ.get("PORT", random.randint(5001, 5999)))

bodega.registrar_en_eureka(PORT)

breaker = pybreaker.CircuitBreaker(fail_max=3, reset_timeout=10)


@app.route('/health')
def health():
    return "OK", 200

@app.route('/bodega', methods=['POST'])
def crear_bodega_ruta():
    data = request.json
    print("DEBUG recibido en /bodega:", data)
    try:
        resultado = bodega.crear_bodega(
            compra_proveedor_id=data['compra_proveedor_id']
        )
        if "error" in resultado:
            return jsonify(resultado), 400
        return jsonify(resultado), 201
    except Exception as e:
        return jsonify({"error": str(e)}), 400



@app.route('/bodega', methods=['GET'])
def obtener_todos_bodega_ruta():
    try:
        registros = bodega.obtener_todos_bodega()
        return jsonify(registros), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/bodega/<int:bodega_id>', methods=['GET'])
def obtener_bodega_por_id_ruta(bodega_id):
    try:
        registro = bodega.obtener_bodega_por_id(bodega_id)
        if registro:
            return jsonify(registro), 200
        else:
            return jsonify({"error": "Registro no encontrado"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/bodega/<int:bodega_id>', methods=['PUT'])
def actualizar_bodega_ruta(bodega_id):
    data = request.json
    try:
        resultado = bodega.actualizar_bodega(
            bodega_id=bodega_id,
            cantidad=data['cantidad'],
            unidad_medida=data['unidad_medida'],
            tipo_insumo=data['tipo_insumo'],
            duracion_insumo=data.get('duracion_insumo')
        )
        return jsonify(resultado), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 400


@app.route('/bodega/<int:bodega_id>', methods=['DELETE'])
def eliminar_bodega_ruta(bodega_id):
    try:
        resultado = bodega.eliminar_bodega(bodega_id)
        return jsonify(resultado), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/inventario-barra', methods=['POST'])
def crear_inventario_barra_ruta():
    datos = request.json
    if not datos:
        return jsonify({"error": "No se envió datos JSON"}), 400
    resultado = inventarioBarra.crear_inventario_barra(datos)
    if "error" in resultado:
        return jsonify(resultado), 400
    return jsonify(resultado), 201


@app.route('/inventario-barra', methods=['GET'])
def obtener_todos_inventario_barra_ruta():
    resultado = inventarioBarra.obtener_todos_inventario_barra()
    if "error" in resultado:
        return jsonify(resultado), 500
    return jsonify(resultado), 200

@app.route('/inventario-barra/<int:id>', methods=['GET'])
def obtener_inventario_barra_por_id_ruta(id):
    resultado = inventarioBarra.obtener_inventario_barra_por_id(id)
    if "error" in resultado:
        return jsonify(resultado), 404
    return jsonify(resultado), 200

@app.route('/inventario-barra/<int:id>', methods=['PUT'])
def actualizar_inventario_barra_ruta(id):
    datos = request.json
    if not datos:
        return jsonify({"error": "No se envió datos JSON"}), 400
    resultado = inventarioBarra.actualizar_inventario_barra(id, datos)
    if "error" in resultado:
        # Asumiendo que error puede ser por no encontrado o mal request
        if "no encontrado" in resultado["error"].lower():
            return jsonify(resultado), 404
        return jsonify(resultado), 400
    return jsonify(resultado), 200

@app.route('/inventario-barra/<int:id>', methods=['DELETE'])
def eliminar_inventario_barra_ruta(id):
    resultado = inventarioBarra.eliminar_inventario_barra(id)
    if "error" in resultado:
        # Si error es por no encontrado, 404; de lo contrario 400
        if "no encontrado" in resultado["error"].lower():
            return jsonify(resultado), 404
        return jsonify(resultado), 400
    return jsonify(resultado), 200




if __name__ == "__main__":
    app.run(port=PORT, debug=False)
