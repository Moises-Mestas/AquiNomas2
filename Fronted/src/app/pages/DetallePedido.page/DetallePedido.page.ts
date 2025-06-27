import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DetallePedidoServices } from '../../core/services/DetallePedido.services';

@Component({
  selector: 'app-detalle-pedido',
  standalone: true,
  templateUrl: './DetallePedido.page.html',
  imports: [CommonModule, FormsModule],  // Asegúrate de importar CommonModule
  styleUrls: ['./DetallePedido.css']
})
export class DetallePedidoPage {
  detallePedidos: any[] = [];
  newDetallePedido: any = { cantidad: 0, menu_id: 0, pedido_id: 0 };
  idAActualizar: number = 0;
  editing: boolean = false;

  constructor(private detallePedidoService: DetallePedidoServices) {}

  ngOnInit() {
    this.getDetallePedidos();
  }

  // Obtener todos los detalles del pedido
  getDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (response) => {
        this.detallePedidos = response;
        console.log(this.detallePedidos);  // Verifica los datos en la consola
      },
      (err) => console.error('Error al obtener los detalles del pedido:', err)
    );
  }

  // Guardar o actualizar detalle de pedido
  saveDetallePedido() {
    if (this.idAActualizar) {
      this.updateDetallePedido();
    } else {
      this.createDetallePedido();
    }
  }

  // Crear detalle de pedido
  createDetallePedido() {
    if (!this.newDetallePedido.menu_id || !this.newDetallePedido.pedido_id || !this.newDetallePedido.cantidad) {
      alert('Faltan campos requeridos.');
      return;
    }

    this.detallePedidoService.createDetallePedido(this.newDetallePedido).subscribe(
      (res) => {
        console.log('Detalle de pedido creado:', res);
        this.getDetallePedidos();
        this.clearForm();
      },
      (err) => console.error('Error al crear detalle de pedido:', err)
    );
  }

  // Actualizar detalle de pedido
  updateDetallePedido() {
    if (!this.idAActualizar) return;

    this.detallePedidoService.updateDetallePedido(this.idAActualizar, this.newDetallePedido).subscribe(
      (res) => {
        console.log('Detalle de pedido actualizado:', res);
        this.getDetallePedidos();
        this.clearForm();
      },
      (err) => console.error('Error al actualizar detalle de pedido:', err)
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
    this.newDetallePedido = { ...detallePedido };
    this.editing = true;
  }

  // Limpiar formulario
  clearForm() {
    this.newDetallePedido = { cantidad: 0, menu_id: 0, pedido_id: 0 };
    this.idAActualizar = 0;
    this.editing = false;
  }
}
