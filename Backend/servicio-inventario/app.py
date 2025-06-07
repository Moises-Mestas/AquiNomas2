from flask import Flask, request, jsonify
import bodega
import pybreaker
import os
import random
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
    try:
        resultado = bodega.crear_bodega(
            compra_proveedor_id=data['compra_proveedor_id'],
            tipo_insumo=data['tipo_insumo'],
            duracion_insumo=data.get('duracion_insumo')
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



if __name__ == "__main__":
    app.run(port=PORT, debug=False)
