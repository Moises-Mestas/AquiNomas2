import mysql.connector
import requests
import py_eureka_client.eureka_client as eureka_client
from mysql.connector import Error


eureka_registered = False


def registrar_en_eureka(puerto):
    global eureka_registered
    if not eureka_registered:
        ip = "127.0.0.1"  

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


def obtener_proveedores():
    """Obtiene todos los proveedores de la base de datos."""
    conexion = None
    cursor = None
    try:
        conexion = obtener_conexion()
        cursor = conexion.cursor(dictionary=True)
        cursor.execute("SELECT * FROM proveedor")
        proveedores = cursor.fetchall()
        return proveedores
    except mysql.connector.Error as e:
        print(f"Error al obtener los proveedores: {e}")
        raise
    finally:
        if cursor:
            cursor.close()
        if conexion:
            conexion.close()


def obtener_proveedor_por_id(proveedor_id):
    """Obtiene un proveedor por su ID."""
    if not isinstance(proveedor_id, int) or proveedor_id <= 0:
        raise ValueError("ID de proveedor inválido.")
    
    conexion = None
    cursor = None
    try:
        conexion = obtener_conexion()
        cursor = conexion.cursor(dictionary=True)
        cursor.execute("SELECT * FROM proveedor WHERE id = %s", (proveedor_id,))
        proveedor = cursor.fetchone()
        return proveedor
    except mysql.connector.Error as e:
        print(f"Error al obtener el proveedor: {e}")
        raise
    finally:
        if cursor:
            cursor.close()
        if conexion:
            conexion.close()


def editar_proveedor(proveedor_id, nombre=None, telefono=None, direccion=None, email=None, ruc=None, estado=None):
    """Edita un proveedor existente en la base de datos."""
    if not isinstance(proveedor_id, int) or proveedor_id <= 0:
        raise ValueError("ID de proveedor inválido.")
    
    if nombre is None and telefono is None and direccion is None and email is None and ruc is None and estado is None:
        raise ValueError("Debe proporcionar al menos un campo para actualizar.")
    
    conexion = None
    cursor = None
    try:
        conexion = obtener_conexion()
        cursor = conexion.cursor()
        campos = []
        valores = []
        
        if nombre is not None:
            campos.append("nombre = %s")
            valores.append(nombre)
        if telefono is not None:
            campos.append("telefono = %s")
            valores.append(telefono)
        if direccion is not None:
            campos.append("direccion = %s")
            valores.append(direccion)
        if email is not None:
            campos.append("email = %s")
            valores.append(email)
        if ruc is not None:
            campos.append("ruc = %s")
            valores.append(ruc)
        if estado is not None:
            campos.append("estado = %s")
            valores.append(estado)
        
        valores.append(proveedor_id)
        consulta = f"UPDATE proveedor SET {', '.join(campos)} WHERE id = %s"
        
        cursor.execute(consulta, tuple(valores))
        conexion.commit()
        print("Proveedor actualizado exitosamente.")
    except mysql.connector.Error as e:
        print(f"Error al editar el proveedor: {e}")
        raise
    finally:
        if cursor:
            cursor.close()
        if conexion:
            conexion.close()


def eliminar_proveedor_por_id(proveedor_id):
    """Elimina un proveedor por su ID."""
    if not isinstance(proveedor_id, int) or proveedor_id <= 0:
        raise ValueError("ID de proveedor inválido.")
    
    conexion = None
    cursor = None
    try:
        conexion = obtener_conexion()
        cursor = conexion.cursor()
        cursor.execute("DELETE FROM proveedor WHERE id = %s", (proveedor_id,))
        conexion.commit()
        print("Proveedor eliminado exitosamente.")
    except mysql.connector.Error as e:
        print(f"Error al eliminar el proveedor: {e}")
        raise
    finally:
        if cursor:
            cursor.close()
        if conexion:
            conexion.close()


def obtener_proveedores_por_estado(estado):
    """Obtiene proveedores filtrados por estado."""
    if not estado or not isinstance(estado, str):
        raise ValueError("El estado es inválido o no se proporcionó.")
    
    conexion = None
    cursor = None
    try:
        conexion = obtener_conexion()
        cursor = conexion.cursor(dictionary=True)
        cursor.execute("SELECT * FROM proveedor WHERE LOWER(estado) = LOWER(%s)", (estado,))
        proveedores = cursor.fetchall()
        return proveedores
    except mysql.connector.Error as e:
        print(f"Error al obtener los proveedores por estado: {e}")
        raise
    finally:
        if cursor:
            cursor.close()
        if conexion:
            conexion.close()
