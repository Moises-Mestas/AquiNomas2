import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DetallePedidoService } from '../../core/services/detallePedido.services';

@Component({
  selector: 'app-detalle-pedido',
  standalone: true,
  templateUrl: './detallePedido.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./detallePedido.page.css']
})
export class DetallePedidoPage {
  detallePedidos: any[] = [];
  newDetallePedido: { pedido_id: number, items: Array<{ menu_id: number, cantidad: number }> } = {
    pedido_id: 0,
    items: [{ menu_id: 0, cantidad: 0 }]
  };
  idAActualizar: number = 0;
  editing: boolean = false;

  constructor(private detallePedidoService: DetallePedidoService) {}

  ngOnInit() {
    this.getDetallePedidos();
    console.log('DetallePedidos en el OnInit:', this.detallePedidos);
  }
// Agregar un nuevo item


// Obtener todos los detalles de pedido
  getDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (response) => {
        this.detallePedidos = response;
        console.log(this.detallePedidos); // Verifica que los datos estén bien asignados
      },
      (err) => console.error('Error al obtener los detalles de pedido:', err)
    );
  }



  // Crear o actualizar detalle de pedido
  saveDetallePedido() {
    console.log('Datos a enviar:', this.newDetallePedido);

    if (this.idAActualizar) {
      // Si existe un ID, significa que estamos editando
      this.updateDetallePedido();
    } else {
      // Si no existe ID, estamos creando un nuevo detallePedido
      this.createDetallePedido();
    }
  }

  // Crear nuevo detallePedido
  createDetallePedido() {
    const detallePedidoData = {
      pedidoId: this.newDetallePedido.pedido_id,
      items: this.newDetallePedido.items.map((item: any) => ({
        menu: { id: item.menu_id },
        cantidad: item.cantidad
      }))
    };

    this.detallePedidoService.createDetallePedido(detallePedidoData).subscribe(
      (res) => {
        console.log('DetallePedido creado:', res);
        this.getDetallePedidos();
        this.clearForm();
      },
      (err) => {
        console.error('Error al crear el detalle de pedido:', err);
        alert('Hubo un error al crear el detalle de pedido');
      }
    );
  }

  // Actualizar detallePedido
  updateDetallePedido() {
    const detallePedidoData = {
      pedidoId: this.newDetallePedido.pedido_id,
      items: this.newDetallePedido.items.map((item: any) => ({
        menu: { id: item.menu_id },
        cantidad: item.cantidad
      }))
    };

    this.detallePedidoService.updateDetallePedido(this.idAActualizar, detallePedidoData).subscribe(
      (res) => {
        console.log('DetallePedido actualizado:', res);
        this.getDetallePedidos();
        this.clearForm();
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
        this.getDetallePedidos();
      },
      (err) => console.error('Error al eliminar detalle de pedido:', err)
    );
  }

  // Editar detalle de pedido
  editDetallePedido(detallePedido: any) {
    this.idAActualizar = detallePedido.id;
    this.newDetallePedido = {
      pedido_id: detallePedido.pedido.id,
      items: detallePedido.items.map((item: any) => ({
        menu_id: item.menu.id,
        cantidad: item.cantidad
      }))
    };
    this.editing = true;
  }

  // Agregar un nuevo item
  addItem() {
    this.newDetallePedido.items.push({ menu_id: 0, cantidad: 0 });
  }

  // Eliminar un item
  removeItem(index: number) {
    this.newDetallePedido.items.splice(index, 1);
  }

  // Limpiar formulario
  clearForm() {
    this.newDetallePedido = { pedido_id: 0, items: [{ menu_id: 0, cantidad: 0 }] };
    this.idAActualizar = 0;
    this.editing = false;
  }
}
