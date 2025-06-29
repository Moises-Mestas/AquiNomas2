import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidoServices } from '../../core/services/pedido.services';
import {ClienteService} from '../../core/services/cliente.services';

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
  clientes: any[] = [];  // Lista de clientes

  // Paginación
  displayedPedidos: any[] = []; // Pedidos mostrados en la página actual
  currentPage: number = 1;
  pedidosPerPage: number = 10;
  totalPages: number = 1;

  constructor(
    private pedidoService: PedidoServices,
    private clienteService: ClienteService  // Inyectar el servicio Cliente
  ) {}
  ngOnInit() {
    this.getPedidos();
    this.getClientes();  // Obtener los clientes al iniciar la página

  }
  getClientes() {
    this.clienteService.getClientes().subscribe(
      (response) => {
        // Ordenar clientes por ID de forma descendente
        this.clientes = response.sort((a: any, b: any) => b.id - a.id);
      },
      (err) => console.error('Error al obtener los clientes:', err)
    );
  }
  // Obtener todos los pedidos
  getPedidos() {
    this.pedidoService.getPedidos().subscribe(
      (response) => {
        this.pedidos = response;
        this.totalPages = Math.ceil(this.pedidos.length / this.pedidosPerPage); // Calcular el número total de páginas
        this.loadPage(this.currentPage); // Cargar los pedidos de la página actual
      },
      (err) => console.error('Error al obtener los pedidos:', err)
    );
  }


  // Cambiar la página actual
  changePage(page: number) {
    if (page < 1 || page > this.totalPages) {
      return; // No cambiar si la página es inválida
    }
    this.currentPage = page;
    this.loadPage(page);
  }

  // Cargar los pedidos de la página seleccionada
  loadPage(page: number) {
    const startIndex = (page - 1) * this.pedidosPerPage;
    const endIndex = page * this.pedidosPerPage;
    this.displayedPedidos = this.pedidos.slice(startIndex, endIndex); // Mostrar solo los pedidos de la página actual
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
