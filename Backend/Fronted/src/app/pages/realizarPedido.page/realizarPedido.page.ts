import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

// Importa los componentes
import { ClientePage } from '../cliente.page/cliente.page';
import { PedidoPage } from '../pedido.page/pedido.page';  // Ruta correcta
import { DetallePedidoPage } from '../DetallePedido.page/DetallePedido.page';
import { PedidoCompletoPage } from '../pedidoCompleto.page/pedidoCompleto.page'; // Importa el nuevo componente

import { ClienteService } from '../../core/services/cliente.services';
import { PedidoServices } from '../../core/services/pedido.services';
import { DetallePedidoService } from '../../core/services/detallePedido.services';

@Component({
  selector: 'app-realizar-pedido',
  standalone: true,
  templateUrl: './realizarPedido.page.html',
  imports: [CommonModule, FormsModule, ClientePage, PedidoPage, DetallePedidoPage, PedidoCompletoPage]  // Asegúrate de incluir los componentes aquí
})
export class RealizarPedidoPage {
  currentView: string = 'cliente'; // Controla la vista actual (cliente, pedido, detallePedido, pedidoCompleto)

  // Aquí puedes agregar las propiedades necesarias
  clientes: any[] = [];
  pedidos: any[] = [];
  detallePedidos: any[] = [];
  newCliente: any = { nombre: '', apellido: '', dni: '', telefono: '', email: '', direccion: '', ruc: '' };
  newPedido: any = { clienteId: 0, estadoPedido: '', fechaPedido: '' };
  newDetallePedido: any = { pedido_id: 0, items: [{ menu_id: 0, cantidad: 0 }] };

  constructor(
    private clienteService: ClienteService,
    private pedidoService: PedidoServices,
    private detallePedidoService: DetallePedidoService
  ) {}

  ngOnInit() {
    this.getClientes();
    this.getPedidos();
    this.getDetallePedidos();
  }

  // Métodos para obtener datos
  getClientes() {
    this.clienteService.getClientes().subscribe(
      (response) => {
        this.clientes = response;
      },
      (err) => console.error('Error al obtener los clientes:', err)
    );
  }

  getPedidos() {
    this.pedidoService.getPedidos().subscribe(
      (response) => {
        this.pedidos = response;
      },
      (err) => console.error('Error al obtener los pedidos:', err)
    );
  }

  getDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (response) => {
        this.detallePedidos = response;
      },
      (err) => console.error('Error al obtener los detalles de pedidos:', err)
    );
  }

  // Método para cambiar la vista
  showView(view: string) {
    this.currentView = view;
  }

  // Métodos para crear o actualizar Cliente, Pedido y DetallePedido
  createCliente() { /* Lógica para crear cliente */ }
  createPedido() { /* Lógica para crear pedido */ }
  createDetallePedido() { /* Lógica para crear detalle de pedido */ }
}
