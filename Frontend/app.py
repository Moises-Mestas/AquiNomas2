from flask import Flask, render_template, request, jsonify
from flask_sqlalchemy import SQLAlchemy

app = Flask(__name__)

# Configuración de la base de datos
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost/pedido_db'
db = SQLAlchemy(app)

# Ruta principal
@app.route('/')
def index():
    return render_template('index.html')

# Ruta para realizar un pedido
@app.route('/api/pedido', methods=['POST'])
def realizar_pedido():
    data = request.get_json()
    menu_id = data.get('menuId')
    cantidad = data.get('cantidad')

    # Lógica para verificar el stock (por ejemplo)
    if menu_id == 1 and cantidad > 5:
        return jsonify({'error': 'No hay suficiente stock para este pedido.'})
    else:
        return jsonify({'success': 'Pedido realizado correctamente.'})

# Asegúrate de que solo tienes una vez esta línea
if __name__ == '__main__':
    app.run(debug=True)
