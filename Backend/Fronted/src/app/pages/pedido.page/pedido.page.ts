import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidoServices } from '../../core/services/pedido.services';

@Component({
  selector: 'app-pedido',
  standalone: true,
  templateUrl: './pedido.page.html',
  imports: [CommonModule, FormsModule], // Agregar CommonModule
  styleUrls: ['./pedido.page.css'],
})
export class PedidoPage {
  pedidos: any[] = [];
  newPedido: any = { clienteId: 0, estadoPedido: '', fechaPedido: '' };
  idAActualizar: number = 0;
  editing: boolean = false;

  constructor(private pedidoService: PedidoServices) {}

  ngOnInit() {
    this.getPedidos();
  }

  // Obtener todos los pedidos
  getPedidos() {
    this.pedidoService.getPedidos().subscribe(
      (response) => {
        this.pedidos = response;
        this.pedidos.sort((a, b) => b.id - a.id);

        console.log(this.pedidos);
      },
      (err) => console.error('Error al obtener los pedidos:', err)
    );
  }

  // Guardar el pedido (crear o actualizar)
  savePedido() {
    if (this.idAActualizar) {
      this.updatePedido();
    } else {
      this.createPedido();
    }
  }

  // Crear pedido
  createPedido() {
    if (!this.newPedido.clienteId || !this.newPedido.estadoPedido || !this.newPedido.fechaPedido) {
      alert('Faltan campos requeridos.');
      return;
    }

    this.pedidoService.createPedido(this.newPedido).subscribe(
      (res) => {
        console.log('Pedido creado:', res);
        this.getPedidos();
        this.clearForm();
      },
      (err) => {
        console.error('Error al crear el pedido:', err);
        alert('Hubo un error al crear el pedido');
      }
    );
  }

  // Actualizar pedido
  updatePedido() {
    if (!this.newPedido.clienteId || !this.newPedido.estadoPedido || !this.newPedido.fechaPedido) {
      alert('Faltan campos requeridos.');
      return;
    }

    this.pedidoService.updatePedido(this.idAActualizar, this.newPedido).subscribe(
      (res) => {
        console.log('Pedido actualizado:', res);
        this.getPedidos();
        this.clearForm();
      },
      (err) => {
        console.error('Error al actualizar el pedido:', err);
        alert('Hubo un error al actualizar el pedido');
      }
    );
  }

  // Eliminar pedido
  deletePedido(id: number) {
    this.pedidoService.deletePedido(id).subscribe(
      (res) => {
        console.log('Pedido eliminado:', res);
        this.getPedidos();
      },
      (err) => console.error('Error al eliminar pedido:', err)
    );
  }

  // Editar pedido
  editPedido(pedido: any) {
    this.idAActualizar = pedido.id;
    this.newPedido = {
      clienteId: pedido.cliente.id, // Asignamos el ID del cliente
      estadoPedido: pedido.estadoPedido,
      fechaPedido: pedido.fechaPedido,
    };
    this.editing = true;
  }

  // Limpiar formulario
  clearForm() {
    this.newPedido = { clienteId: 0, estadoPedido: '', fechaPedido: '' };
    this.idAActualizar = 0;
    this.editing = false;
  }
}
