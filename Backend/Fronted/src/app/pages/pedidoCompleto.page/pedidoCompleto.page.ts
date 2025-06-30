import { Component } from '@angular/core';
import { PedidoServices } from '../../core/services/pedido.services';
import { ClienteService } from '../../core/services/cliente.services';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-pedido-completo',
  standalone: true,
  templateUrl: './pedidoCompleto.page.html',
  styleUrls: ['./pedidoCompleto.page.css'],
  imports: [CommonModule, FormsModule]  // Asegúrate de agregar estos módulos
})
export class PedidoCompletoPage {
  pedidos: any[] = []; // Todos los pedidos
  displayedPedidos: any[] = []; // Pedidos que se muestran en la página actual
  estadoFiltro: string = '';  // Filtro para el estado de los pedidos
  selectedPedido: any = null; // Pedido seleccionado para mostrar en el modal

  // Paginación
  currentPage: number = 1;
  pedidosPerPage: number = 16;  // 4 pedidos por fila (en tarjetas)
  totalPages: number = 1;

  constructor(
    private pedidoService: PedidoServices,
    private clienteService: ClienteService
  ) {}

  ngOnInit() {
    this.getPedidos(); // Cargar los pedidos al iniciar la página
  }

  // Obtener los pedidos
  getPedidos() {
    if (this.estadoFiltro) {
      // Si hay un filtro por estado, obtener solo los pedidos filtrados
      this.pedidoService.getPedidosPorEstado(this.estadoFiltro).subscribe(
        (response) => {
          this.pedidos = response;

          // Ordenar los pedidos por ID de manera descendente (más reciente a más antiguo)
          this.pedidos.sort((a: any, b: any) => b.id - a.id);

          this.totalPages = Math.ceil(this.pedidos.length / this.pedidosPerPage);
          this.loadPage(this.currentPage);
        },
        (err) => console.error('Error al obtener los pedidos:', err)
      );
    } else {
      // Si no hay filtro, obtener todos los pedidos
      this.pedidoService.getPedidos().subscribe(
        (response) => {
          this.pedidos = response;

          // Ordenar los pedidos por ID de manera descendente (más reciente a más antiguo)
          this.pedidos.sort((a: any, b: any) => b.id - a.id);

          this.totalPages = Math.ceil(this.pedidos.length / this.pedidosPerPage);
          this.loadPage(this.currentPage);
        },
        (err) => console.error('Error al obtener los pedidos:', err)
      );
    }
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

  // Abrir el modal con los detalles del pedido seleccionado
  openModal(pedido: any) {
    this.selectedPedido = pedido;
  }

  // Cerrar el modal
  closeModal() {
    this.selectedPedido = null;
  }

  // Filtrar pedidos por estado
  filterPedidos() {
    this.getPedidos(); // Volver a cargar los pedidos con el estado seleccionado
  }
}
