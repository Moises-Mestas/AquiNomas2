from flask import Flask, render_template, request, redirect, url_for
from flask_sqlalchemy import SQLAlchemy
from datetime import datetime

app = Flask(__name__)

# Configuración de la base de datos
app.config['SQLALCHEMY_DATABASE_URI'] = 'mysql+pymysql://root:@localhost/pedido_db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)

# Modelo de Pedido
class Pedido(db.Model):
    __tablename__ = 'pedido'
    id = db.Column(db.Integer, primary_key=True)
    cliente_id = db.Column(db.Integer, nullable=False)
    estado_pedido = db.Column(db.String(100), nullable=False)
    fecha_pedido = db.Column(db.DateTime, nullable=False)

@app.route('/')
def index():
    page = request.args.get('page', 1, type=int)
    pedidos = Pedido.query.paginate(page=page, per_page=10, error_out=False)
    return render_template('index.html', pedidos=pedidos)

# Ruta para filtrar pedidos por estado
@app.route('/pedidos/filter', methods=['GET'])
def listar_por_estado():
    estado = request.args.get('estadoPedido')  # Obtén el parámetro de consulta
    page = request.args.get('page', 1, type=int)

    if estado == "TODOS" or not estado:
        pedidos = Pedido.query.paginate(page=page, per_page=10, error_out=False)
    else:
        pedidos = Pedido.query.filter(Pedido.estado_pedido == estado).paginate(page=page, per_page=10, error_out=False)

    return render_template('index.html', pedidos=pedidos)

# Ruta para eliminar un pedido
@app.route('/eliminar/<int:id>', methods=['GET'])
def eliminar_pedido(id):
    pedido = Pedido.query.get(id)
    if pedido:
        db.session.delete(pedido)
        db.session.commit()
    return redirect(url_for('index'))

# Ruta para crear un pedido
@app.route('/crear', methods=['POST'])
def crear_pedido():
    cliente_id = request.form['cliente_id']
    estado_pedido = request.form['estado_pedido']
    fecha_pedido = request.form['fecha_pedido']
    
    nuevo_pedido = Pedido(cliente_id=cliente_id, estado_pedido=estado_pedido, fecha_pedido=fecha_pedido)
    db.session.add(nuevo_pedido)
    db.session.commit()
    
    return redirect(url_for('index'))

# Ruta para editar un pedido
@app.route('/editar/<int:id>', methods=['GET', 'POST'])
def editar_pedido(id):
    pedido = Pedido.query.get_or_404(id)

    if request.method == 'POST':
        pedido.cliente_id = request.form['cliente_id']
        pedido.estado_pedido = request.form['estado_pedido']
        pedido.fecha_pedido = datetime.strptime(request.form['fecha_pedido'], '%Y-%m-%dT%H:%M')

        db.session.commit()
        return redirect(url_for('index'))

    return render_template('index.html', pedido=pedido)

if __name__ == '__main__':
    app.run(debug=True)
