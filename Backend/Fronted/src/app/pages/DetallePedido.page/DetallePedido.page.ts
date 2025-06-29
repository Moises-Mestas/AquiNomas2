import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DetallePedidoService } from '../../core/services/detallePedido.services';  // Asegúrate de tener el servicio importado

@Component({
  selector: 'app-detalle-pedido',
  standalone: true,
  templateUrl: './DetallePedido.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./DetallePedido.page.css']
})
export class DetallePedidoPage {
  detallePedidos: any[] = [];
  newDetallePedido: any = { cantidad: 0, menu_id: 0, pedido_id: 0 };  // Incluimos pedido_id
  idAActualizar: number = 0;
  editing: boolean = false;

  constructor(private detallePedidoService: DetallePedidoService) {}

  ngOnInit() {
    this.getDetallePedidos();
  }

  // Obtener todos los detalles de pedido
  getDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (response) => {
        this.detallePedidos = response;
        console.log(this.detallePedidos); // Verifica los datos en la consola
      },
      (err) => console.error('Error al obtener los detalles de pedido:', err)
    );
  }

  saveDetallePedido() {
    console.log('Datos a enviar:', this.newDetallePedido);

    if (this.idAActualizar) {
      // Si existe un ID, significa que estamos editando
      console.log("Actualizando detalle:", this.newDetallePedido);
      this.updateDetallePedido();
    } else {
      // Si no existe ID, estamos creando un nuevo detallePedido
      console.log("Creando detalle:", this.newDetallePedido);
      this.createDetallePedido();
    }
  }


  // Crear DetallePedido
  createDetallePedido() {
    console.log("Datos a crear:", this.newDetallePedido); // Verificar datos

    // Verificar que todos los campos estén presentes
    if (!this.newDetallePedido.menu_id || !this.newDetallePedido.pedido_id || !this.newDetallePedido.cantidad) {
      console.error('Datos faltantes:', this.newDetallePedido);
      alert('Faltan campos requeridos.');
      return;
    }

    // Estructura correcta para el backend
    const detallePedidoData = {
      pedidoId: this.newDetallePedido.pedido_id, // Asegúrate de usar el campo pedidoId
      items: [
        {
          menu: { id: this.newDetallePedido.menu_id },
          cantidad: this.newDetallePedido.cantidad
        }
      ]
    };

    // Enviar los datos en el formato correcto
    this.detallePedidoService.createDetallePedido(detallePedidoData).subscribe(
      (res) => {
        console.log('DetallePedido creado:', res);
        this.getDetallePedidos(); // Refrescar la lista de detalles de pedido
        this.clearForm(); // Limpiar el formulario
      },
      (err) => {
        console.error('Error al crear el detalle de pedido:', err);
        alert('Hubo un error al crear el detalle de pedido');
      }
    );
  }

  // Actualizar DetallePedido
  updateDetallePedido() {
    console.log("Datos a actualizar:", this.newDetallePedido);

    if (!this.idAActualizar) {
      console.error('No se especificó el ID del detalle de pedido');
      return;
    }

    if (!this.newDetallePedido.menu_id || !this.newDetallePedido.pedido_id || !this.newDetallePedido.cantidad) {
      console.error('Datos faltantes:', this.newDetallePedido);
      alert('Faltan campos requeridos.');
      return;
    }

    this.detallePedidoService.updateDetallePedido(this.idAActualizar, this.newDetallePedido).subscribe(
      (res) => {
        console.log('DetallePedido actualizado:', res);
        this.getDetallePedidos(); // Refresca la lista de detalles de pedido
        this.clearForm(); // Limpia el formulario
      },
      (err) => {
        console.error('Error al actualizar el detalle de pedido:', err);
        alert('Hubo un error al actualizar el detalle de pedido');
      }
    );
  }

  // Eliminar detalle de pedido
  deleteDetallePedido(id: number) {
    this.detallePedidoService.deleteDetallePedido(id).subscribe(
      (res) => {
        console.log('Detalle de pedido eliminado:', res);
        this.getDetallePedidos();  // Refrescar la lista de detalles de pedido
      },
      (err) => console.error('Error al eliminar detalle de pedido:', err)
    );
  }

  editDetallePedido(detallePedido: any) {
    if (!detallePedido) {
      console.error('No se ha recibido el detallePedido');
      return;
    }

    // Asignamos los valores al formulario, incluyendo el pedido_id
    this.idAActualizar = detallePedido.id;

    this.newDetallePedido = {
      id: detallePedido.id,
      cantidad: detallePedido.cantidad,
      menu_id: detallePedido.menu?.id, // Asegúrate de que 'menu' exista
      pedido_id: detallePedido.pedido?.id // Asegúrate de que 'pedido' exista
    };

    console.log('Detalle a editar:', this.newDetallePedido);
    this.editing = true;
  }


  // Limpiar formulario
  clearForm() {
    this.newDetallePedido = { cantidad: 0, menu_id: 0, pedido_id: 0 };
    this.idAActualizar = 0;
    this.editing = false;
  }
}
