
import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client

eureka_registered = False


def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        ip = "127.0.0.1"  

        import py_eureka_client.eureka_client as eureka_client
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",
            app_name="servicio-inventario",
            instance_id=f"servicio-inventario-{puerto}",
            health_check_url=f"http://{ip}:{puerto}/health",
            home_page_url=f"http://{ip}:{puerto}",
            instance_host=ip,
            instance_port=puerto,
        )
        eureka_registered = True
        print(f"Instancia registrada en Eureka correctamente: http://{ip}:{puerto}")

def cargar_configuracion(app_name, profile="default", config_server_url="http://localhost:7070"):
    url = f"{config_server_url}/{app_name}/{profile}"
    response = requests.get(url, auth=("root", "123456"))

    if response.status_code == 200:
        config = response.json()
        print("CONFIG OBTENIDA:", config)
        propiedades = config.get("propertySources", [])
        
        resultado = {}
        for fuente in propiedades:
            resultado.update(fuente["source"])
        
        return resultado
    else:
        print("Error al obtener la configuración del servidor:", response.status_code)
        return {}

def obtener_conexion():
    config = cargar_configuracion("servicio-inventario")

    db_url = config.get("spring.jpa.datasource.url", "")

    if not db_url.startswith("jdbc:mysql://"):
        raise ValueError("La URL de la base de datos no está configurada correctamente.")
    
    try:
        host = db_url.split("://")[1].split("/")[0].split(":")[0]
        port = db_url.split("://")[1].split("/")[0].split(":")[1]
        database = db_url.split("/")[-1]
    except IndexError:
        raise ValueError("Error al analizar la URL de la base de datos.")
    
    user = config.get("spring.jpa.datasource.username", "root")
    password = config.get("spring.jpa.datasource.password", "")

    return mysql.connector.connect(
        host=host,
        port=port,
        user=user,
        password=password,
        database=database
    )


def crear_inventario_cocina_desde_bodega(bodega_id, cantidad_disponible, stock_minimo=0, nombre_producto=None, unidad_destino="kg"):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("SELECT * FROM bodega WHERE id = %s", (bodega_id,))
            bodega_data = cursor.fetchone()

            if not bodega_data:
                return {"error": "El 'bodega_id' no existe en la tabla 'bodega'."}

            if bodega_data["tipo_insumo"].lower() not in ["comida", "alimento"]:
                return {"error": "Solo se pueden ingresar datos con 'tipo_insumo' igual a 'comida' o 'alimento'."}

            cantidad_bodega_convertida = convertir_unidad(bodega_data["cantidad"], bodega_data["unidad_medida"], unidad_destino)
            cantidad_disponible_convertida = convertir_unidad(cantidad_disponible, unidad_destino, unidad_destino)

            if cantidad_bodega_convertida < cantidad_disponible_convertida:
                return {"error": "La cantidad disponible en bodega es insuficiente."}

            nombre_producto = nombre_producto or bodega_data.get("producto_id", "Producto desconocido")

            cursor.execute("SELECT * FROM inventario_cocina WHERE bodega_id = %s", (bodega_id,))
            inventario_cocina_data = cursor.fetchone()

            if inventario_cocina_data:
                cantidad_existente_convertida = convertir_unidad(
                    inventario_cocina_data["cantidad_disponible"],
                    inventario_cocina_data["unidad_medida"],
                    unidad_destino
                )

                nueva_cantidad = cantidad_existente_convertida + cantidad_disponible_convertida

                cursor.execute("""
                    UPDATE inventario_cocina
                    SET cantidad_disponible = %s, ultima_fecha_entrada = NOW(), unidad_medida = %s
                    WHERE bodega_id = %s
                """, (nueva_cantidad, unidad_destino, bodega_id))
            else:
                cursor.execute("""
                    INSERT INTO inventario_cocina (bodega_id, producto_id, cantidad_disponible, unidad_medida, stock_minimo, ultima_fecha_entrada)
                    VALUES (%s, %s, %s, %s, %s, NOW())
                """, (
                    bodega_id,
                    bodega_data.get("producto_id"),
                    cantidad_disponible_convertida,
                    unidad_destino,
                    stock_minimo
                ))

            nueva_cantidad_bodega = cantidad_bodega_convertida - cantidad_disponible_convertida
            nueva_cantidad_bodega_original = convertir_unidad(nueva_cantidad_bodega, unidad_destino, bodega_data["unidad_medida"])
            cursor.execute("""
                UPDATE bodega
                SET cantidad = %s, fecha_movimiento = NOW()
                WHERE id = %s
            """, (nueva_cantidad_bodega_original, bodega_id))

            conexion.commit()

        return {"mensaje": f"Registro actualizado o creado correctamente en inventario_cocina y cantidad descontada en bodega en {unidad_destino}."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()



def convertir_unidad(cantidad, unidad_origen, unidad_destino):
    """
    Convierte una cantidad de una unidad a otra.
    Soporta 'g' y 'kg'.
    """
    if unidad_origen == unidad_destino:
        return cantidad

    if unidad_origen == "kg" and unidad_destino == "g":
        return cantidad * 1000  
    elif unidad_origen == "g" and unidad_destino == "kg":
        return cantidad / 1000  
    else:
        raise ValueError(f"Conversión no soportada de {unidad_origen} a {unidad_destino}")

def obtener_todos_inventario_cocina():
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, bodega_id, producto_id, cantidad_disponible, unidad_medida, stock_minimo, ultima_fecha_entrada, ultima_fecha_salida
                FROM inventario_cocina
            """)
            registros = cursor.fetchall()
        return registros
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()

def obtener_inventario_cocina_por_id(id):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, bodega_id, producto_id, cantidad_disponible, unidad_medida, stock_minimo, ultima_fecha_entrada, ultima_fecha_salida
                FROM inventario_cocina
                WHERE id = %s
            """, (id,))
            registro = cursor.fetchone()
        if registro:
            return registro
        else:
            return {"error": "Registro no encontrado."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()




def actualizar_inventario_cocina(id, datos):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("""
                UPDATE inventario_cocina
                SET cantidad_disponible = %s, unidad_medida = %s, stock_minimo = %s, ultima_fecha_salida = NOW()
                WHERE id = %s
            """, (
                datos["cantidad_disponible"],
                datos["unidad_medida"],
                datos.get("stock_minimo", 0),  
                id
            ))
            conexion.commit()
        return {"mensaje": "Registro actualizado correctamente."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()



def eliminar_inventario_cocina(id):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("DELETE FROM inventario_cocina WHERE id = %s", (id,))
            conexion.commit()
        return {"mensaje": "Registro eliminado correctamente."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()
def alerta_stock_minimo_cocina():
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, bodega_id, producto_id, cantidad_disponible, unidad_medida, stock_minimo, ultima_fecha_entrada, ultima_fecha_salida
                FROM inventario_cocina
                WHERE cantidad_disponible <= stock_minimo
            """)
            productos_en_alerta = cursor.fetchall()

        if productos_en_alerta:
            return {"alertas": productos_en_alerta}
        else:
            return {"mensaje": "No hay productos con stock mínimo alcanzado en cocina."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()