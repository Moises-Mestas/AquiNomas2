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

  // Paginación
  displayedDetallePedidos: any[] = []; // Detalles de pedidos mostrados en la página actual
  currentPage: number = 1;
  detallePedidosPerPage: number = 10; // Limitar a 10 detalles por página
  totalPages: number = 1;

  constructor(private detallePedidoService: DetallePedidoService) {}

  ngOnInit() {
    this.getDetallePedidos();
  }

  // Obtener todos los detalles de pedido
  getDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (response) => {
        // Ordenar los detalles por ID de más reciente a más antiguo (descendente)
        this.detallePedidos = response.sort((a: any, b: any) => b.id - a.id);

        // Calcular el número total de páginas
        this.totalPages = Math.ceil(this.detallePedidos.length / this.detallePedidosPerPage);

        // Cargar los detalles de pedido de la página actual
        this.loadPage(this.currentPage);
      },
      (err) => console.error('Error al obtener los detalles de pedido:', err)
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

  // Cargar los detalles de pedido de la página seleccionada
  loadPage(page: number) {
    const startIndex = (page - 1) * this.detallePedidosPerPage;
    const endIndex = page * this.detallePedidosPerPage;
    this.displayedDetallePedidos = this.detallePedidos.slice(startIndex, endIndex); // Mostrar solo los detalles de pedido de la página actual
  }

  // Crear o actualizar detalle de pedido
  saveDetallePedido() {
    console.log('Datos a enviar:', this.newDetallePedido);

    if (this.idAActualizar) {
      this.updateDetallePedido();
    } else {
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
