import pymysql

# Intenta conectarte a la base de datos
try:
    connection = pymysql.connect(
        host='localhost',
        user='root',        # Asegúrate de que el usuario esté configurado correctamente
        password='',  # Asegúrate de que la contraseña sea correcta
        database='producto_db'   # La base de datos a la que te quieres conectar
    )
    print("Conexión exitosa a la base de datos")
except pymysql.MySQLError as e:
    print(f"Error al conectar a la base de datos: {e}")
finally:
    if connection:
        connection.close()  # Cierra la conexión si fue exitosa
