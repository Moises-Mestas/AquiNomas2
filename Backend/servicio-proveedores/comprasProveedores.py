
import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client
import requests
import xml.etree.ElementTree as ET
from mysql.connector import Error

eureka_registered = False


def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        ip = "127.0.0.1"  

        import py_eureka_client.eureka_client as eureka_client
        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",
            app_name="SERVICIO-PROVEEDOR",
            instance_id=f"servicio-proveedor-{puerto}",
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
    """Obtiene una conexión a la base de datos."""
    config = cargar_configuracion("servicio-proveedor")
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


from decimal import Decimal

UNIDADES_CONVERSION = {
    'kg': 1,      
    'g': 0.001,   
    'l': 1,       
    'ml': 0.001,  
    'unidad': 1   
}

def crear_compra_proveedor(proveedor_id, producto_id, cantidad, unidad_medida):
    """Crea una nueva compra de proveedor en la base de datos y la registra en bodega."""
    if not isinstance(proveedor_id, int) or not isinstance(producto_id, int):
        raise ValueError("IDs de proveedor y producto deben ser enteros.")
    if not isinstance(cantidad, (int, float)) or cantidad <= 0:
        raise ValueError("La cantidad debe ser un número positivo.")
    if unidad_medida not in UNIDADES_CONVERSION:
        raise ValueError(f"Unidad de medida inválida. Debe ser una de: {', '.join(UNIDADES_CONVERSION.keys())}")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            # Obtener el precio del producto
            cursor.execute("SELECT precio, tipo_insumo, duracion_insumo FROM producto WHERE id = %s", (producto_id,))
            producto = cursor.fetchone()
            if not producto:
                raise ValueError("El producto con el ID proporcionado no existe.")
            
            precio_unitario = Decimal(producto['precio'])
            tipo_insumo = producto['tipo_insumo']
            duracion_insumo = producto['duracion_insumo']
            
            cantidad_decimal = Decimal(cantidad) * Decimal(UNIDADES_CONVERSION[unidad_medida])  # Convertir a unidad base
            total = cantidad_decimal * precio_unitario  

            # Crear la compra en la base de datos
            cursor.execute("""
                INSERT INTO compra_proveedor (proveedor_id, producto_id, cantidad, unidad_medida, total)
                VALUES (%s, %s, %s, %s, %s)
            """, (proveedor_id, producto_id, cantidad, unidad_medida, total))
            conexion.commit()
            compra_proveedor_id = cursor.lastrowid  # Obtener el ID de la compra recién creada

        # Registrar la compra en la bodega del servicio-inventario
        resultado_bodega = crear_registro_en_bodega_desde_proveedor(compra_proveedor_id)
        if "error" in resultado_bodega:
            print(f"Error al registrar en bodega: {resultado_bodega['error']}")

        print("Compra de proveedor creada y registrada en bodega exitosamente.")
        return compra_proveedor_id
    except mysql.connector.Error as e:
        print(f"Error al crear la compra de proveedor: {e}")
        raise
    finally:
        if conexion:
            conexion.close()


def obtener_compras_proveedores():
    """Obtiene todas las compras de proveedores con los datos de proveedor, producto y total."""
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT cp.id, cp.cantidad, cp.unidad_medida, cp.total, cp.fecha_compra,
                       p.nombre AS proveedor_nombre, p.email AS proveedor_email,
                       pr.nombre AS producto_nombre, pr.precio AS producto_precio
                FROM compra_proveedor cp
                JOIN proveedor p ON cp.proveedor_id = p.id
                JOIN producto pr ON cp.producto_id = pr.id
            """)
            compras = cursor.fetchall()
        return compras
    except mysql.connector.Error as e:
        print(f"Error al obtener las compras de proveedores: {e}")
        raise
    finally:
        if conexion:
            conexion.close()


def obtener_compra_proveedor_por_id(compra_id):
    if not isinstance(compra_id, int) or compra_id <= 0:
        raise ValueError("ID de compra inválido.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT cp.id, cp.cantidad, cp.unidad_medida, cp.total, cp.fecha_compra, cp.producto_id,
                       p.nombre AS proveedor_nombre, p.email AS proveedor_email,
                       pr.nombre AS producto_nombre, pr.precio AS producto_precio, pr.id as producto_id_2
                FROM compra_proveedor cp
                JOIN proveedor p ON cp.proveedor_id = p.id
                JOIN producto pr ON cp.producto_id = pr.id
                WHERE cp.id = %s
            """, (compra_id,))
            compra = cursor.fetchone()
            print("DEBUG compra:", compra)
        return compra
    except mysql.connector.Error as e:
        print(f"Error al obtener la compra de proveedor: {e}")
        raise
    finally:
        if conexion:
            conexion.close()




URL_SERVICIO_INVENTARIO = None

def obtener_url_servicio_inventario():
    global URL_SERVICIO_INVENTARIO
    try:
        response = requests.get("http://localhost:8090/eureka/apps/SERVICIO-INVENTARIO", headers={"Accept": "application/xml"})
        if response.status_code == 200:
            root = ET.fromstring(response.content)

            # Busca todas las instancias registradas bajo ese nombre
            instances = root.findall('.//instance')
            if not instances:
                print("No se encontraron instancias registradas para SERVICIO-INVENTARIO.")
                return None

            # Toma la primera instancia disponible
            instance = instances[0]
            host = instance.find('hostName').text
            port = instance.find('port').text
            URL_SERVICIO_INVENTARIO = f"http://{host}:{port}"
            print(f"[DEBUG] URL_SERVICIO_INVENTARIO: {URL_SERVICIO_INVENTARIO}")
            return URL_SERVICIO_INVENTARIO
        else:
            print(f"[ERROR] Código de estado al consultar Eureka: {response.status_code}")
            return None
    except Exception as e:
        print(f"[EXCEPCIÓN] Error al obtener URL del servicio-inventario: {e}")
        return None

obtener_url_servicio_inventario()


# def crear_registro_en_bodega_desde_proveedor(compra_proveedor_id, tipo_insumo=None, duracion_insumo=None):
#     global URL_SERVICIO_INVENTARIO
#     if not URL_SERVICIO_INVENTARIO:
#         if not obtener_url_servicio_inventario():
#             return {"error": "No se pudo resolver la URL del servicio-inventario"}

#     url_bodega = f"{URL_SERVICIO_INVENTARIO}/bodega"
#     payload = {
#         "compra_proveedor_id": compra_proveedor_id
#     }

#     print("DEBUG: Payload enviado al servicio-inventario:", payload)

#     try:
#         response = requests.post(url_bodega, json=payload, timeout=5)
#         response.raise_for_status()
#         return response.json()
#     except requests.exceptions.Timeout:
#         print("Error: La solicitud al servicio-inventario excedió el tiempo de espera.")
#         return {"error": "Timeout al contactar con servicio-inventario"}
#     except requests.exceptions.RequestException as e:
#         print(f"Error al contactar con servicio-inventario: {e}")
#         obtener_url_servicio_inventario()
#         return {"error": f"Error al contactar con servicio-inventario: {str(e)}"}


def crear_registro_en_bodega_desde_proveedor(compra_proveedor_id, tipo_insumo=None, duracion_insumo=None):
    global URL_SERVICIO_INVENTARIO
    if not URL_SERVICIO_INVENTARIO:
        if not obtener_url_servicio_inventario():
            return {"error": "No se pudo resolver la URL del servicio-inventario"}

    # Obtener el producto asociado a la compra
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT p.nombre AS nombre_producto
                FROM compra_proveedor cp
                JOIN producto p ON cp.producto_id = p.id
                WHERE cp.id = %s
            """, (compra_proveedor_id,))
            producto_data = cursor.fetchone()

            if not producto_data:
                return {"error": "No se encontró el producto asociado a la compra_proveedor_id."}

            nombre_producto = producto_data["nombre_producto"]

    finally:
        if conexion:
            conexion.close()

    # Construir el payload con el nombre del producto
    url_bodega = f"{URL_SERVICIO_INVENTARIO}/bodega"
    payload = {
        "compra_proveedor_id": compra_proveedor_id,
        "nombre_producto": nombre_producto  # Incluir el nombre del producto
    }

    print("DEBUG: Payload enviado al servicio-inventario:", payload)

    try:
        response = requests.post(url_bodega, json=payload, timeout=5)
        response.raise_for_status()
        return response.json()
    except requests.exceptions.Timeout:
        print("Error: La solicitud al servicio-inventario excedió el tiempo de espera.")
        return {"error": "Timeout al contactar con servicio-inventario"}
    except requests.exceptions.RequestException as e:
        print(f"Error al contactar con servicio-inventario: {e}")
        obtener_url_servicio_inventario()
        return {"error": f"Error al contactar con servicio-inventario: {str(e)}"}