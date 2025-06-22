import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PedidoService } from '../../core/services/pedido.service';
import { Pedido, EstadoPedido } from '../../core/resources/pedido.model';
import { Cliente } from '../../core/resources/cliente.model';

@Component({
  standalone: true,
  selector: 'app-pedido',
  templateUrl: './pedido.page.html',
  styleUrls: ['./pedido.page.scss'],
  imports: [CommonModule, FormsModule]
})
export class PedidoPage implements OnInit {
  pedidos: Pedido[] = [];
  formPedido: Pedido = {
    clienteId: 0,
    estadoPedido: 'PENDIENTE'
  };
  editing: boolean = false;

  estadoOpciones: EstadoPedido[] = ['PENDIENTE', 'INICIADO', 'COMPLETADO', 'CANCELADO'];

  constructor(private pedidoService: PedidoService) {}

  ngOnInit() {
    this.loadPedidos();
  }

  loadPedidos() {
    this.pedidoService.getPedidos().subscribe(data => this.pedidos = data);
  }

  startEdit(pedido: Pedido) {
    this.formPedido = { ...pedido };
    this.editing = true;
  }

  savePedido() {
    if (this.editing && this.formPedido.id) {
      this.pedidoService.updatePedido(this.formPedido.id, this.formPedido).subscribe(() => {
        this.loadPedidos();
        this.cancel();
      });
    } else {
      this.pedidoService.createPedido(this.formPedido).subscribe(() => {
        this.loadPedidos();
        this.cancel();
      });
    }
  }

  deletePedido(id: number) {
    if (confirm('¿Seguro de eliminar este pedido?')) {
      this.pedidoService.deletePedido(id).subscribe(() => this.loadPedidos());
    }
  }

  cancel() {
    this.formPedido = {
      clienteId: 0,
      estadoPedido: 'PENDIENTE',
      fechaPedido: new Date().toISOString().slice(0,16) // Solo yyyy-MM-ddTHH:mm
    };
    this.editing = false;
  }

}
