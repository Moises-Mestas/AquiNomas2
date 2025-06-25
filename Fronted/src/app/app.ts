import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { PedidoService } from './core/services/pedido.services';
import { MenuService } from './core/services/menu.services';
import { DetallePedidoService } from './core/services/detalle-pedido.services';
import { RecetaService } from './core/services/receta.services';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  title = 'Frontend';
  pedidos: any[] = [];
  menus: any[] = [];
  detallePedidos: any[] = [];
  recetas: any[] = [];
  nuevoPedido: any = {};  // Para crear un nuevo pedido
  nuevoMenu: any = {};    // Para crear un nuevo menú
  nuevoDetalle: any = {}; // Para crear un nuevo detalle de pedido
  nuevaReceta: any = {};  // Para crear una nueva receta

  constructor(
    private pedidoService: PedidoService,
    private menuService: MenuService,
    private detallePedidoService: DetallePedidoService,
    private recetaService: RecetaService
  ) {}

  ngOnInit() {
    this.cargarPedidos();
    this.cargarMenus();
    this.cargarDetallePedidos();
    this.cargarRecetas();
  }

  cargarPedidos() {
    this.pedidoService.getPedidos().subscribe(
      (data) => { this.pedidos = data; },
      (error) => { console.error('Error al cargar los pedidos', error); }
    );
  }

  cargarMenus() {
    this.menuService.getMenus().subscribe(
      (data) => { this.menus = data; },
      (error) => { console.error('Error al cargar los menús', error); }
    );
  }

  cargarDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (data) => { this.detallePedidos = data; },
      (error) => { console.error('Error al cargar los detalles de los pedidos', error); }
    );
  }

  cargarRecetas() {
    this.recetaService.getRecetas().subscribe(
      (data) => { this.recetas = data; },
      (error) => { console.error('Error al cargar las recetas', error); }
    );
  }

  // Crear un nuevo pedido
  crearPedido() {
    this.pedidoService.crearPedido(this.nuevoPedido).subscribe(
      (data) => { this.cargarPedidos(); },
      (error) => { console.error('Error al crear el pedido', error); }
    );
  }

  // Crear un nuevo menú
  crearMenu() {
    this.menuService.crearMenu(this.nuevoMenu).subscribe(
      (data) => { this.cargarMenus(); },
      (error) => { console.error('Error al crear el menú', error); }
    );
  }

  // Crear un nuevo detalle de pedido
  crearDetallePedido() {
    this.detallePedidoService.crearDetallePedido(this.nuevoDetalle).subscribe(
      (data) => { this.cargarDetallePedidos(); },
      (error) => { console.error('Error al crear el detalle del pedido', error); }
    );
  }

  // Crear una nueva receta
  crearReceta() {
    this.recetaService.crearReceta(this.nuevaReceta).subscribe(
      (data) => { this.cargarRecetas(); },
      (error) => { console.error('Error al crear la receta', error); }
    );
  }

  // Eliminar un pedido
  eliminarPedido(id: number) {
    this.pedidoService.eliminarPedido(id).subscribe(
      (response) => { this.cargarPedidos(); },
      (error) => { console.error('Error al eliminar el pedido', error); }
    );
  }

  // Eliminar un menú
  eliminarMenu(id: number) {
    this.menuService.eliminarMenu(id).subscribe(
      (response) => { this.cargarMenus(); },
      (error) => { console.error('Error al eliminar el menú', error); }
    );
  }

  // Eliminar un detalle de pedido
  eliminarDetallePedido(id: number) {
    this.detallePedidoService.eliminarDetallePedido(id).subscribe(
      (response) => { this.cargarDetallePedidos(); },
      (error) => { console.error('Error al eliminar el detalle del pedido', error); }
    );
  }

  // Eliminar una receta
  eliminarReceta(id: number) {
    this.recetaService.eliminarReceta(id).subscribe(
      (response) => { this.cargarRecetas(); },
      (error) => { console.error('Error al eliminar la receta', error); }
    );
  }
}
