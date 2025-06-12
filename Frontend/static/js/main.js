document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('pedidoForm');
    const messageDiv = document.getElementById('message');

    form.addEventListener('submit', function (e) {
        e.preventDefault();  // Evita que se recargue la página al enviar el formulario

        const menuId = document.getElementById('menuId').value;
        const cantidad = document.getElementById('cantidad').value;

        // Limpiar mensaje previo
        messageDiv.innerHTML = '';

        // Validación del formulario
        if (!menuId || !cantidad) {
            messageDiv.innerHTML = '<div class="alert alert-danger">Por favor, complete todos los campos.</div>';
            return;
        }

        // Hacer la petición AJAX a Flask (API)
        fetch('/api/pedido', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                menuId: menuId,
                cantidad: cantidad
            })
        })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                messageDiv.innerHTML = `<div class="alert alert-danger">${data.error}</div>`;
            } else {
                messageDiv.innerHTML = `<div class="alert alert-success">Pedido realizado correctamente.</div>`;
            }
        })
        .catch(error => {
            messageDiv.innerHTML = `<div class="alert alert-danger">Hubo un error en la solicitud. Intenta de nuevo.</div>`;
        });
    });
});
