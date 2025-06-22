import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DetallePedidoService } from '../../core/services/detalle-pedido.service';
import { MenuService } from '../../core/services/menu.service';
import { PedidoService } from '../../core/services/pedido.service';
import { DetallePedido } from '../../core/resources/detalle-pedido.model';
import { Menu } from '../../core/resources/menu.model';
import { Pedido } from '../../core/resources/pedido.model';

@Component({
  standalone: true,
  selector: 'app-detalle-pedido',
  templateUrl: './detalle-pedido.page.html',
  styleUrls: ['./detalle-pedido.page.scss'],
  imports: [CommonModule, FormsModule]
})
export class DetallePedidoPage implements OnInit {
  detalles: DetallePedido[] = [];
  menus: Menu[] = [];
  pedidos: Pedido[] = [];
  formDetalle: DetallePedido = {
    pedido: { id: 0 } as Pedido,
    menu: {} as Menu,
    cantidad: 1
  };
  editing: boolean = false;

  constructor(
    private detalleService: DetallePedidoService,
    private menuService: MenuService,
    private pedidoService: PedidoService
  ) {}

  ngOnInit() {
    this.loadDetalles();
    this.menuService.getMenus().subscribe(data => this.menus = data);
    this.pedidoService.getPedidos().subscribe(data => this.pedidos = data);
  }

  loadDetalles() {
    this.detalleService.getDetalles().subscribe(data => this.detalles = data);
  }

  startEdit(detalle: DetallePedido) {
    this.formDetalle = { ...detalle, menu: detalle.menu, pedido: detalle.pedido };
    this.editing = true;
  }

  saveDetalle() {
    if (this.editing && this.formDetalle.id) {
      this.detalleService.updateDetalle(this.formDetalle.id, this.formDetalle).subscribe(() => {
        this.loadDetalles();
        this.cancel();
      });
    } else {
      this.detalleService.createDetalle(this.formDetalle).subscribe(() => {
        this.loadDetalles();
        this.cancel();
      });
    }
  }

  deleteDetalle(id: number) {
    if (confirm('¿Seguro de eliminar este detalle?')) {
      this.detalleService.deleteDetalle(id).subscribe(() => this.loadDetalles());
    }
  }

  cancel() {
    this.formDetalle = {
      pedido: { id: 0 } as Pedido,
      menu: {} as Menu,
      cantidad: 1
    };
    this.editing = false;
  }
}
