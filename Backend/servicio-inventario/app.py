from flask import Flask, request, jsonify
import bodega
import pybreaker
import os
from bodega import *
import random
import inventarioBarra
from tenacity import retry, stop_after_attempt, wait_fixed
from inventarioBarra import alerta_stock_minimo
from inventarioCocina import *
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




@app.route('/bodega/tipo-insumo/<string:tipo_insumo>', methods=['GET'])
def ruta_obtener_bodega_por_tipo_insumo(tipo_insumo):
    try:
        registros = obtener_bodega_por_tipo_insumo(tipo_insumo)
        if registros:
            return jsonify(registros), 200
        else:
            return jsonify({"error": "No se encontraron registros en bodega para el tipo_insumo especificado."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500





@app.route('/bodega/fecha-entrada', methods=['GET'])
def ruta_obtener_bodega_por_fecha_entrada():
    try:
        fecha_entrada = request.args.get('fecha_entrada')

        if not fecha_entrada:
            return jsonify({"error": "Debe proporcionar el parámetro 'fecha_entrada'."}), 400

        registros = obtener_bodega_por_fecha_entrada(fecha_entrada)
        if registros:
            return jsonify(registros), 200
        else:
            return jsonify({"error": "No se encontraron registros en bodega para la fecha especificada."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/bodega/rango-fecha-entrada', methods=['GET'])
def ruta_obtener_bodega_por_rango_fecha_entrada():
    try:
        fecha_inicio = request.args.get('fecha_inicio')
        fecha_fin = request.args.get('fecha_fin')

        if not fecha_inicio or not fecha_fin:
            return jsonify({"error": "Debe proporcionar los parámetros 'fecha_inicio' y 'fecha_fin'."}), 400

        registros = obtener_bodega_por_rango_fecha_entrada(fecha_inicio, fecha_fin)
        if registros:
            return jsonify(registros), 200
        else:
            return jsonify({"error": "No se encontraron registros en bodega para el rango de fechas especificado."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/bodega/producto/<int:producto_id>', methods=['GET'])
def ruta_obtener_bodega_por_producto_id(producto_id):
    try:
        registros = obtener_bodega_por_producto_id(producto_id)
        if registros:
            return jsonify(registros), 200
        else:
            return jsonify({"error": "No se encontraron registros en bodega para el producto_id especificado."}), 404
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    

@app.route('/bodega/stock-total/<int:producto_id>', methods=['GET'])
def ruta_obtener_stock_total_por_producto(producto_id):
    try:
        resultado = obtener_stock_total_por_producto(producto_id)
        return jsonify(resultado), 200
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    


@app.route('/bodega/stock-bajo', methods=['GET'])
def ruta_obtener_productos_con_stock_bajo():
    try:
        stock_minimo = request.args.get('stock_minimo', type=float)
        if stock_minimo is None:
            return jsonify({"error": "Debe proporcionar el parámetro 'stock_minimo'."}), 400

        productos = obtener_productos_con_stock_bajo(stock_minimo)
        return jsonify(productos), 200
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500


@app.route('/bodega/historial/<int:producto_id>', methods=['GET'])
def ruta_obtener_historial_movimientos_por_producto(producto_id):
    try:
        movimientos = obtener_historial_movimientos_por_producto(producto_id)
        return jsonify(movimientos), 200
    except ValueError as e:
        return jsonify({"error": str(e)}), 400
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    




@app.route('/inventario-barra/desde-bodega', methods=['POST'])
def crear_inventario_barra_desde_bodega_ruta():
    datos = request.json
    if not datos or "bodega_id" not in datos or "cantidad_disponible" not in datos:
        return jsonify({"error": "Se requiere 'bodega_id' y 'cantidad_disponible'."}), 400

    resultado = inventarioBarra.crear_inventario_barra_desde_bodega(
        bodega_id=datos["bodega_id"],
        cantidad_disponible=datos["cantidad_disponible"],
        stock_minimo=datos.get("stock_minimo", 0), 
        unidad_destino=datos.get("unidad_destino", "l")  
    )

    if "error" in resultado:
        return jsonify(resultado), 400
    return jsonify(resultado), 201

@app.route('/inventario-barra', methods=['GET'])
def obtener_todos_inventario_barra_ruta():
    try:
        registros = inventarioBarra.obtener_todos_inventario_barra()
        return jsonify(registros), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    

@app.route('/inventario-barra/<int:id>', methods=['GET'])
def obtener_inventario_barra_por_id_ruta(id):
    try:
        registro = inventarioBarra.obtener_inventario_barra_por_id(id)
        if registro:
            return jsonify(registro), 200
        else:
            return jsonify({"error": "Registro no encontrado"}), 404
    except Exception as e:
        return jsonify({"error": str(e)}), 500
    

@app.route('/inventario-barra/<int:id>', methods=['DELETE'])
def eliminar_inventario_barra_ruta(id):
    try:
        resultado = inventarioBarra.eliminar_inventario_barra(id)
        if "error" in resultado:
            return jsonify(resultado), 400
        return jsonify(resultado), 200
    except Exception as e:
        return jsonify({"error": str(e)}), 500   
    



@app.route('/inventario-cocina/crear', methods=['POST'])
def ruta_crear_inventario_cocina_desde_bodega():
    data = request.json
    bodega_id = data.get("bodega_id")
    cantidad_disponible = data.get("cantidad_disponible")
    stock_minimo = data.get("stock_minimo", 0)
    unidad_destino = data.get("unidad_destino", "kg")  

    resultado = crear_inventario_cocina_desde_bodega(
        bodega_id=bodega_id,
        cantidad_disponible=cantidad_disponible,
        stock_minimo=stock_minimo,
        unidad_destino=unidad_destino
    )
    return jsonify(resultado)


# Ruta para obtener todos los registros de inventario_cocina
@app.route('/inventario-cocina', methods=['GET'])
def ruta_obtener_todos_inventario_cocina():
    resultado = obtener_todos_inventario_cocina()
    return jsonify(resultado)

# Ruta para obtener un registro específico de inventario_cocina por ID
@app.route('/inventario-cocina/<int:id>', methods=['GET'])
def ruta_obtener_inventario_cocina_por_id(id):
    resultado = obtener_inventario_cocina_por_id(id)
    return jsonify(resultado)

# Ruta para actualizar un registro de inventario_cocina
@app.route('/inventario-cocina/<int:id>', methods=['PUT'])
def ruta_actualizar_inventario_cocina(id):
    datos = request.json
    resultado = actualizar_inventario_cocina(id, datos)
    return jsonify(resultado)

# Ruta para eliminar un registro de inventario_cocina
@app.route('/inventario-cocina/<int:id>', methods=['DELETE'])
def ruta_eliminar_inventario_cocina(id):
    resultado = eliminar_inventario_cocina(id)
    return jsonify(resultado)



@app.route('/inventario-barra/alerta-stock-minimo', methods=['GET'])
def ruta_alerta_stock_minimo():
    resultado = alerta_stock_minimo()
    return jsonify(resultado)




@app.route('/inventario-cocina/alerta-stock-minimo', methods=['GET'])
def ruta_alerta_stock_minimo_cocina():
    resultado = alerta_stock_minimo_cocina()
    return jsonify(resultado)



if __name__ == "__main__":
    app.run(host="0.0.0.0", port=PORT, debug=False)
