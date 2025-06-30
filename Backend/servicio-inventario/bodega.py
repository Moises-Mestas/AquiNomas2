import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client
import xml.etree.ElementTree as ET
from datetime import datetime
eureka_registered = False


def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        ip = "127.0.0.1"

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


URL_SERVICIO_PROVEEDOR = None

def obtener_url_servicio_proveedor():
    global URL_SERVICIO_PROVEEDOR
    try:
        response = requests.get("http://localhost:8090/eureka/apps/SERVICIO-PROVEEDOR")
        if response.status_code == 200:
            root = ET.fromstring(response.content)
            instance = root.find('instance')
            if instance is not None:
                host = instance.find('hostName').text
                port = instance.find('port').text
                URL_SERVICIO_PROVEEDOR = f"http://{host}:{port}"
                print(f"URL servicio-proveedor actualizada: {URL_SERVICIO_PROVEEDOR}")
                return URL_SERVICIO_PROVEEDOR
            else:
                print("No se encontró la instancia en la respuesta de Eureka.")
                URL_SERVICIO_PROVEEDOR = None
                return None
        else:
            print(f"Error al consultar Eureka: status {response.status_code}")
            URL_SERVICIO_PROVEEDOR = None
            return None
    except Exception as e:
        print(f"Error al obtener URL del servicio-proveedor: {e}")
        URL_SERVICIO_PROVEEDOR = None
        return None

# Inicializar la URL al cargar el módulo
obtener_url_servicio_proveedor()

def obtener_compra_proveedor_por_id(compra_proveedor_id):
    global URL_SERVICIO_PROVEEDOR
    if not URL_SERVICIO_PROVEEDOR:
        if not obtener_url_servicio_proveedor():
            return {"error": "No se pudo resolver la URL del servicio-proveedor"}

    try:
        response = requests.get(f"{URL_SERVICIO_PROVEEDOR}/compras-proveedores/{compra_proveedor_id}", timeout=5)
        if response.status_code == 200:
            return response.json()
        else:
            return {"error": f"Compra de proveedor con ID {compra_proveedor_id} no encontrada"}
    except Exception as e:
        print(f"Error al contactar con servicio-proveedor: {str(e)}")
        obtener_url_servicio_proveedor()  # Reintenta actualizar URL
        return {"error": "Error al contactar con servicio-proveedor"}





def obtener_producto_por_id_desde_servicio(producto_id):
    global URL_SERVICIO_PROVEEDOR
    if not URL_SERVICIO_PROVEEDOR:
        if not obtener_url_servicio_proveedor():
            return {"error": "No se pudo resolver la URL del servicio-proveedor"}

    try:
        url = f"{URL_SERVICIO_PROVEEDOR}/productos/{producto_id}"
        response = requests.get(url, timeout=5)
        if response.status_code == 200:
            return response.json()  
        elif response.status_code == 404:
            print(f"Producto con ID {producto_id} no encontrado en el servicio-proveedor")
            return None
        else:
            print(f"Error al obtener producto: {response.status_code}")
            return None
    except requests.exceptions.RequestException as e:
        print(f"Error en la conexión al servicio de productos: {e}")
        return None
    



def obtener_todos_bodega():
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, compra_proveedor_id, producto_id, cantidad, unidad_medida, tipo_insumo, duracion_insumo, fecha_entrada, fecha_movimiento
                FROM bodega
            """)
            registros = cursor.fetchall()

        for registro in registros:
            producto = obtener_producto_por_id_desde_servicio(registro["producto_id"])
            if producto and "nombre" in producto:
                registro["nombre_producto"] = producto["nombre"]
            else:
                registro["nombre_producto"] = "Producto desconocido"

            compra = obtener_compra_proveedor_por_id(registro["compra_proveedor_id"])
            if "error" not in compra:
                registro["proveedor_nombre"] = compra.get("proveedor_nombre")
            else:
                registro["proveedor_nombre"] = None

        return registros
    except mysql.connector.Error as e:
        print(f"Error al obtener los datos de bodega: {e}")
        raise
    finally:
        if conexion:
            conexion.close()




def convertir_a_kg(cantidad, unidad_medida):
    if unidad_medida.lower() == 'g': 
        return cantidad / 1000.0
    elif unidad_medida.lower() == 'kg':  
        return cantidad
    elif unidad_medida.lower() == 'l': 
        return cantidad
    elif unidad_medida.lower() == 'ml':  
        return cantidad / 1000.0
    else:
        raise ValueError(f"Unidad de medida no soportada: {unidad_medida}")

def parse_fecha_compra(fecha_str):
    try:
        if ', ' in fecha_str:
            fecha_str_clean = fecha_str.split(', ')[1].replace(' GMT', '')
        else:
            fecha_str_clean = fecha_str.replace(' GMT', '')

        fecha_dt = datetime.strptime(fecha_str_clean, '%d %b %Y %H:%M:%S')
        return fecha_dt.strftime('%Y-%m-%d %H:%M:%S')
    except Exception as e:
        print(f"DEBUG error en parse_fecha_compra: {e}")
        raise

def crear_bodega(compra_proveedor_id):
    conexion = None
    try:
        compra = obtener_compra_proveedor_por_id(compra_proveedor_id)
        print("DEBUG compra:", compra)

        if "error" in compra:
            return {"error": compra["error"]}

        cantidad = float(compra.get("cantidad")) / 2
        unidad_medida = compra.get("unidad_medida")
        fecha_compra = compra.get("fecha_compra")
        producto_id = compra.get("producto_id")

        print(f"DEBUG cantidad ajustada: {cantidad}, unidad_medida: {unidad_medida}, fecha_compra: {fecha_compra}, producto_id: {producto_id}")

        if cantidad is None or unidad_medida is None or fecha_compra is None or producto_id is None:
            return {"error": "Datos incompletos en la compra para crear bodega"}

        producto = obtener_producto_por_id_desde_servicio(producto_id)
        print("DEBUG producto:", producto)

        if not producto:
            return {"error": f"El producto con ID {producto_id} no existe en el servicio-proveedores"}

        tipo_insumo = producto.get("tipo_insumo")
        duracion_insumo = producto.get("duracion_insumo")

        if not tipo_insumo or not duracion_insumo:
            return {"error": "El producto no tiene configurados los campos tipo_insumo o duracion_insumo"}

        cantidad_en_kg = convertir_a_kg(cantidad, unidad_medida)
        print(f"DEBUG cantidad_en_kg ajustada: {cantidad_en_kg}")

        fecha_compra_formateada = parse_fecha_compra(fecha_compra)

        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("SELECT id, cantidad FROM bodega WHERE producto_id = %s", (producto_id,))
            existente = cursor.fetchone()

            if existente:
                bodega_id = existente[0]
                cantidad_existente = float(existente[1])
                nueva_cantidad = cantidad_existente + cantidad_en_kg

                cursor.execute("""
                    UPDATE bodega SET cantidad = %s, fecha_entrada = %s
                    WHERE id = %s
                """, (nueva_cantidad, fecha_compra_formateada, bodega_id))
                mensaje = "Cantidad actualizada en bodega (producto repetido)"
            else:
                cursor.execute("""
                    INSERT INTO bodega (compra_proveedor_id, producto_id, cantidad, unidad_medida, tipo_insumo, duracion_insumo, fecha_entrada)
                    VALUES (%s, %s, %s, %s, %s, %s, %s)
                """, (
                    compra_proveedor_id,
                    producto_id,
                    cantidad_en_kg,
                    unidad_medida,
                    tipo_insumo,
                    duracion_insumo,
                    fecha_compra_formateada
                ))
                mensaje = "Registro nuevo creado en bodega"

            conexion.commit()
            return {"mensaje": mensaje}

    except mysql.connector.Error as e:
        print(f"DEBUG Error al crear el registro en bodega: {e}")
        return {"error": "Error al crear el registro en bodega"}
    except ValueError as ve:
        print(f"DEBUG Error de conversión de unidad: {ve}")
        return {"error": str(ve)}
    finally:
        if conexion:
            conexion.close()


def actualizar_bodega(bodega_id, cantidad, unidad_medida, tipo_insumo, duracion_insumo):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("""
                UPDATE bodega
                SET cantidad = %s, unidad_medida = %s, tipo_insumo = %s, duracion_insumo = %s, fecha_movimiento = NOW()
                WHERE id = %s
            """, (cantidad, unidad_medida, tipo_insumo, duracion_insumo, bodega_id))
            conexion.commit()
            return {"mensaje": "Registro actualizado exitosamente en bodega"}
    except mysql.connector.Error as e:
        print(f"Error al actualizar el registro en bodega: {e}")
        return {"error": "Error al actualizar el registro en bodega"}
    finally:
        if conexion:
            conexion.close()

def eliminar_bodega(bodega_id):
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("DELETE FROM bodega WHERE id = %s", (bodega_id,))
            conexion.commit()
            return {"mensaje": "Registro eliminado exitosamente de bodega"}
    except mysql.connector.Error as e:
        print(f"Error al eliminar el registro de bodega: {e}")
        return {"error": "Error al eliminar el registro de bodega"}
    finally:
        if conexion:
            conexion.close()


def obtener_bodega_por_tipo_insumo(tipo_insumo):
    """Obtiene registros de bodega filtrados por tipo_insumo."""
    if not tipo_insumo or not isinstance(tipo_insumo, str):
        raise ValueError("El tipo_insumo es inválido o no se proporcionó.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, compra_proveedor_id, producto_id, cantidad, unidad_medida, tipo_insumo, duracion_insumo, fecha_entrada, fecha_movimiento
                FROM bodega
                WHERE LOWER(tipo_insumo) = LOWER(%s)
            """, (tipo_insumo,))
            registros = cursor.fetchall()

        for registro in registros:
            producto = obtener_producto_por_id_desde_servicio(registro["producto_id"])
            if producto and "nombre" in producto:
                registro["nombre_producto"] = producto["nombre"]
            else:
                registro["nombre_producto"] = "Producto desconocido"

            compra = obtener_compra_proveedor_por_id(registro["compra_proveedor_id"])
            if "error" not in compra:
                registro["proveedor_nombre"] = compra.get("proveedor_nombre")
            else:
                registro["proveedor_nombre"] = None

        return registros
    except mysql.connector.Error as e:
        print(f"Error al obtener los registros de bodega por tipo_insumo: {e}")
        raise
    finally:
        if conexion:
            conexion.close()


def obtener_bodega_por_fecha_entrada(fecha_entrada):
    """Obtiene registros de bodega filtrados por una fecha específica de entrada."""
    if not fecha_entrada:
        raise ValueError("Debe proporcionar una fecha válida.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, compra_proveedor_id, producto_id, cantidad, unidad_medida, tipo_insumo, duracion_insumo, fecha_entrada, fecha_movimiento
                FROM bodega
                WHERE DATE(fecha_entrada) = %s
            """, (fecha_entrada,))
            registros = cursor.fetchall()
        return registros
    except mysql.connector.Error as e:
        print(f"Error al obtener los registros de bodega por fecha de entrada: {e}")
        raise
    finally:
        if conexion:
            conexion.close()           


def obtener_bodega_por_rango_fecha_entrada(fecha_inicio, fecha_fin):
    """Obtiene registros de bodega filtrados por un rango de fechas de entrada."""
    if not fecha_inicio or not fecha_fin:
        raise ValueError("Debe proporcionar ambas fechas: fecha_inicio y fecha_fin.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, compra_proveedor_id, producto_id, cantidad, unidad_medida, tipo_insumo, duracion_insumo, fecha_entrada, fecha_movimiento
                FROM bodega
                WHERE fecha_entrada BETWEEN %s AND %s
            """, (fecha_inicio, fecha_fin))
            registros = cursor.fetchall()
        return registros
    except mysql.connector.Error as e:
        print(f"Error al obtener los registros de bodega por rango de fechas de entrada: {e}")
        raise
    finally:
        if conexion:
            conexion.close()




def obtener_bodega_por_producto_id(producto_id):
    """Obtiene registros de bodega filtrados por producto_id."""
    if not producto_id or not isinstance(producto_id, int):
        raise ValueError("El producto_id es inválido o no se proporcionó.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, compra_proveedor_id, producto_id, cantidad, unidad_medida, tipo_insumo, duracion_insumo, fecha_entrada, fecha_movimiento
                FROM bodega
                WHERE producto_id = %s
            """, (producto_id,))
            registros = cursor.fetchall()
        for registro in registros:
            producto = obtener_producto_por_id_desde_servicio(registro["producto_id"])
            if producto and "nombre" in producto:
                registro["nombre_producto"] = producto["nombre"]
            else:
                registro["nombre_producto"] = "Producto desconocido"

            compra = obtener_compra_proveedor_por_id(registro["compra_proveedor_id"])
            if "error" not in compra:
                registro["proveedor_nombre"] = compra.get("proveedor_nombre")
            else:
                registro["proveedor_nombre"] = None

        return registros
    except mysql.connector.Error as e:
        print(f"Error al obtener los registros de bodega por producto_id: {e}")
        raise
    finally:
        if conexion:
            conexion.close()        

            

def obtener_stock_total_por_producto(producto_id):
    """Obtiene el stock total de un producto en la bodega."""
    if not producto_id or not isinstance(producto_id, int):
        raise ValueError("El producto_id es inválido o no se proporcionó.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT SUM(cantidad) AS stock_total, unidad_medida
                FROM bodega
                WHERE producto_id = %s
                GROUP BY unidad_medida
            """, (producto_id,))
            resultado = cursor.fetchone()
        return resultado if resultado else {"mensaje": "No hay stock para el producto especificado."}
    except mysql.connector.Error as e:
        print(f"Error al obtener el stock total por producto: {e}")
        raise
    finally:
        if conexion:
            conexion.close()           


def obtener_productos_con_stock_bajo(stock_minimo):
    """Obtiene productos con stock por debajo del nivel mínimo."""
    if not isinstance(stock_minimo, (int, float)) or stock_minimo < 0:
        raise ValueError("El stock mínimo debe ser un número positivo.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT producto_id, SUM(cantidad) AS stock_total, unidad_medida
                FROM bodega
                GROUP BY producto_id, unidad_medida
                HAVING stock_total < %s
            """, (stock_minimo,))
            productos = cursor.fetchall()
        return productos if productos else {"mensaje": "No hay productos con stock bajo."}
    except mysql.connector.Error as e:
        print(f"Error al obtener productos con stock bajo: {e}")
        raise
    finally:
        if conexion:
            conexion.close()            



def obtener_historial_movimientos_por_producto(producto_id):
    """Obtiene el historial de movimientos de un producto en la bodega."""
    if not producto_id or not isinstance(producto_id, int):
        raise ValueError("El producto_id es inválido o no se proporcionó.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, cantidad, unidad_medida, fecha_entrada, fecha_movimiento
                FROM bodega
                WHERE producto_id = %s
                ORDER BY fecha_movimiento DESC
            """, (producto_id,))
            movimientos = cursor.fetchall()
        return movimientos if movimientos else {"mensaje": "No hay movimientos registrados para este producto."}
    except mysql.connector.Error as e:
        print(f"Error al obtener el historial de movimientos: {e}")
        raise
    finally:
        if conexion:
            conexion.close()            