
import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client
import datetime
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




def crear_inventario_barra_desde_bodega(bodega_id, cantidad_disponible, stock_minimo=0, nombre_producto=None, unidad_destino="l"):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("SELECT * FROM bodega WHERE id = %s", (bodega_id,))
            bodega_data = cursor.fetchone()

            if not bodega_data:
                return {"error": "El 'bodega_id' no existe en la tabla 'bodega'."}

            if bodega_data["tipo_insumo"].lower() not in ["bebida", "bebidas"]:
                return {"error": "Solo se pueden ingresar datos con 'tipo_insumo' igual a 'bebida' o 'bebidas'."}

            cantidad_bodega_convertida = convertir_unidad(bodega_data["cantidad"], bodega_data["unidad_medida"], unidad_destino)
            cantidad_disponible_convertida = convertir_unidad(cantidad_disponible, unidad_destino, unidad_destino)

            if cantidad_bodega_convertida < cantidad_disponible_convertida:
                return {"error": "La cantidad disponible en bodega es insuficiente."}

            nombre_producto = nombre_producto or bodega_data.get("producto_id", "Producto desconocido")

            cursor.execute("SELECT * FROM inventario_barra WHERE bodega_id = %s", (bodega_id,))
            inventario_barra_data = cursor.fetchone()

            if inventario_barra_data:
                cantidad_existente_convertida = convertir_unidad(
                inventario_barra_data["cantidad_disponible"],
                inventario_barra_data["unidad_medida"],
                unidad_destino
                )
                nueva_cantidad = cantidad_existente_convertida + cantidad_disponible_convertida
                cursor.execute("""
                UPDATE inventario_barra
                SET cantidad_disponible = %s, fecha_entrada = NOW(), nombre_producto = %s, unidad_medida = %s
                WHERE bodega_id = %s
                """, (nueva_cantidad, nombre_producto, unidad_destino, bodega_id))
            else:
                cursor.execute("""
                    INSERT INTO inventario_barra (bodega_id, cantidad_disponible, unidad_medida, stock_minimo, fecha_entrada, nombre_producto)
                    VALUES (%s, %s, %s, %s, NOW(), %s)
                """, (
                    bodega_id,
                    cantidad_disponible_convertida,
                    unidad_destino,
                    stock_minimo,
                    nombre_producto
                ))

            nueva_cantidad_bodega = cantidad_bodega_convertida - cantidad_disponible_convertida
            nueva_cantidad_bodega_original = convertir_unidad(nueva_cantidad_bodega, unidad_destino, bodega_data["unidad_medida"])
            cursor.execute("""
                UPDATE bodega
                SET cantidad = %s, fecha_movimiento = NOW()
                WHERE id = %s
            """, (nueva_cantidad_bodega_original, bodega_id))

            conexion.commit()

        return {"mensaje": f"Registro actualizado o creado correctamente en inventario_barra y cantidad descontada en bodega en {unidad_destino}."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()
def convertir_unidad(cantidad, unidad_origen, unidad_destino):
    """
    Convierte una cantidad de una unidad a otra.
    Soporta 'l' y 'ml'.
    """
    if unidad_origen == unidad_destino:
        return cantidad

    if unidad_origen == "l" and unidad_destino == "ml":
        return cantidad * 1000  
    elif unidad_origen == "ml" and unidad_destino == "l":
        return cantidad / 1000  
    else:
        raise ValueError(f"Conversión no soportada de {unidad_origen} a {unidad_destino}")
    

def obtener_todos_inventario_barra():
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("SELECT * FROM inventario_barra")
            registros = cursor.fetchall()
        return registros
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()


def obtener_inventario_barra_por_id(id):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("SELECT * FROM inventario_barra WHERE id = %s", (id,))
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


def actualizar_inventario_barra(id, datos):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("""
                UPDATE inventario_barra
                SET cantidad_disponible = %s, unidad_medida = %s, stock_minimo = %s
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


def eliminar_inventario_barra(id):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("DELETE FROM inventario_barra WHERE id = %s", (id,))
            conexion.commit()
        return {"mensaje": "Registro eliminado correctamente."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()




def alerta_stock_minimo():
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, bodega_id, cantidad_disponible, stock_minimo, nombre_producto
                FROM inventario_barra
                WHERE cantidad_disponible <= stock_minimo
            """)
            productos_en_alerta = cursor.fetchall()

        if productos_en_alerta:
            return {"alertas": productos_en_alerta}
        else:
            return {"mensaje": "No hay productos con stock mínimo alcanzado."}
    except mysql.connector.Error as e:
        return {"error": f"Error en la base de datos: {e}"}
    finally:
        if conexion:
            conexion.close()