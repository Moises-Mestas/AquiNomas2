
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


        eureka_client.init(
            eureka_server="http://localhost:8090/eureka",
            app_name="servicio-proveedor",
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



def crear_producto(nombre, descripcion, precio, tipo_insumo, duracion_insumo):
    """Crea un nuevo producto en la base de datos."""
    if not nombre or not isinstance(precio, (int, float)) or precio < 0:
        raise ValueError("Datos inválidos para crear el producto.")
    
    if not tipo_insumo or not duracion_insumo:
        raise ValueError("Los campos tipo_insumo y duracion_insumo son obligatorios.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("""
                INSERT INTO producto (nombre, descripcion, precio, tipo_insumo, duracion_insumo)
                VALUES (%s, %s, %s, %s, %s)
            """, (nombre, descripcion, precio, tipo_insumo, duracion_insumo))
            conexion.commit()
            print("Producto creado exitosamente.")
    except Error as e:
        print(f"Error al crear el producto: {e}")
        raise
    finally:
        if conexion:
            conexion.close()

def obtener_productos():
    """Obtiene todos los productos de la base de datos."""
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("SELECT * FROM producto")
            productos = cursor.fetchall()
        return productos
    except Error as e:
        print(f"Error al obtener los productos: {e}")
        raise
    finally:
        if conexion:
            conexion.close()

def obtener_producto_por_id(producto_id):
    """Obtiene un producto por su ID."""
    if not isinstance(producto_id, int) or producto_id <= 0:
        raise ValueError("ID de producto inválido.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, nombre, descripcion, precio, tipo_insumo, duracion_insumo 
                FROM producto WHERE id = %s
            """, (producto_id,))
            producto = cursor.fetchone()
        return producto
    except Error as e:
        print(f"Error al obtener el producto: {e}")
        raise
    finally:
        if conexion:
            conexion.close()

def editar_producto(producto_id, nombre=None, descripcion=None, precio=None, tipo_insumo=None, duracion_insumo=None):
    """Edita un producto existente en la base de datos."""
    if not isinstance(producto_id, int) or producto_id <= 0:
        raise ValueError("ID de producto inválido.")
    
    if all(param is None for param in [nombre, descripcion, precio, tipo_insumo, duracion_insumo]):
        raise ValueError("Debe proporcionar al menos un campo para actualizar.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            campos = []
            valores = []
            
            if nombre is not None:
                campos.append("nombre = %s")
                valores.append(nombre)
            if descripcion is not None:
                campos.append("descripcion = %s")
                valores.append(descripcion)
            if precio is not None:
                if not isinstance(precio, (int, float)) or precio < 0:
                    raise ValueError("El precio debe ser un número positivo.")
                campos.append("precio = %s")
                valores.append(precio)
            if tipo_insumo is not None:
                campos.append("tipo_insumo = %s")
                valores.append(tipo_insumo)
            if duracion_insumo is not None:
                campos.append("duracion_insumo = %s")
                valores.append(duracion_insumo)
            
            valores.append(producto_id)
            consulta = f"UPDATE producto SET {', '.join(campos)} WHERE id = %s"
            
            cursor.execute(consulta, tuple(valores))
            conexion.commit()
            print("Producto actualizado exitosamente.")
    except Error as e:
        print(f"Error al editar el producto: {e}")
        raise
    finally:
        if conexion:
            conexion.close()

def eliminar_producto_por_id(producto_id):
    """Elimina un producto por su ID."""
    if not isinstance(producto_id, int) or producto_id <= 0:
        raise ValueError("ID de producto inválido.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor() as cursor:
            cursor.execute("DELETE FROM producto WHERE id = %s", (producto_id,))
            conexion.commit()
            print("Producto eliminado exitosamente.")
    except Error as e:
        print(f"Error al eliminar el producto: {e}")
        raise
    finally:
        if conexion:
            conexion.close()



def obtener_producto_por_nombre(nombre_producto):
    """Obtiene un producto por su nombre."""
    if not isinstance(nombre_producto, str) or not nombre_producto.strip():
        raise ValueError("El nombre del producto es inválido.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, nombre, descripcion, precio, tipo_insumo, duracion_insumo 
                FROM producto WHERE LOWER(nombre) = LOWER(%s)
            """, (nombre_producto,))
            producto = cursor.fetchall()
        return producto
    except Error as e:
        print(f"Error al obtener el producto por nombre: {e}")
        raise
    finally:
        if conexion:
            conexion.close()


def obtener_productos_por_tipo_insumo(tipo_insumo):
    """Obtiene productos por su tipo de insumo."""
    if not isinstance(tipo_insumo, str) or not tipo_insumo.strip():
        raise ValueError("El tipo de insumo es inválido.")
    
    conexion = None
    try:
        print(f"Buscando productos con tipo_insumo: {tipo_insumo}")
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, nombre, descripcion, precio, tipo_insumo, duracion_insumo 
                FROM producto WHERE LOWER(tipo_insumo) = LOWER(%s)
            """, (tipo_insumo,))
            productos = cursor.fetchall()
        return productos
    except Error as e:
        print(f"Error al obtener los productos por tipo de insumo: {e}")
        raise
    finally:
        if conexion:
            conexion.close()



def obtener_productos_por_rango_precio(precio_min, precio_max):
    """Obtiene productos dentro de un rango de precios."""
    if not isinstance(precio_min, (int, float)) or not isinstance(precio_max, (int, float)):
        raise ValueError("Los valores de precio_min y precio_max deben ser números.")
    if precio_min < 0 or precio_max < 0 or precio_min > precio_max:
        raise ValueError("El rango de precios es inválido.")
    
    conexion = None
    try:
        conexion = obtener_conexion()
        with conexion.cursor(dictionary=True) as cursor:
            cursor.execute("""
                SELECT id, nombre, descripcion, precio, tipo_insumo, duracion_insumo 
                FROM producto WHERE precio BETWEEN %s AND %s
            """, (precio_min, precio_max))
            productos = cursor.fetchall()
        return productos
    except Error as e:
        print(f"Error al obtener los productos por rango de precio: {e}")
        raise
    finally:
        if conexion:
            conexion.close()