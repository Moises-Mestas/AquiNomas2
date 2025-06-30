import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MenuService } from './core/services/menu.services';
import { RecetaService } from './core/services/receta.services';
import { MenuPage } from './pages/menu.page/menu.page';
import { RecetaPage } from './pages/receta.page/receta.page';
import { PedidoServices } from './core/services/pedido.services';
import { DetallePedidoService } from './core/services/detallePedido.services';
import { ProductoServices } from './core/services/producto.services';
import { AuthService } from './core/services/auth.services';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, CommonModule],
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
  protected title = 'Fronted';

  // Menús
  idAEliminar: number = 0;
  idAActualizar: number = 0;
  newMenu: any = {
    nombre: '',
    descripcion: '',
    precio: 0,
    tipo: '',
    imagen: '',
  };
  menus: any[] = [];

  // Recetas
  recetaIdAEliminar: number = 0;
  recetaIdAActualizar: number = 0;
  newReceta: any = {
    cantidad: 0,
    descripcion: '',
    producto_id: 0,
    unidadMedida: '',
    menu_id: 0,
    cantidadDisponible: 0,
  };
  recetas: any[] = [];

  // Pedidos
  pedidoIdAEliminar: number = 0;
  pedidoIdAActualizar: number = 0;
  newPedido: any = { cliente_id: 0, estado_pedido: '', fecha_pedido: '' };
  pedidos: any[] = [];

  // Detalle de pedidos
  detallePedidoIdAEliminar: number = 0;
  detallePedidoIdAActualizar: number = 0;
  newDetallePedido: any = { cantidad: 0, menu_id: 0, pedido_id: 0 };
  detallePedidos: any[] = [];

  // Productos
  productoIdAEliminar: number = 0;
  productoIdAActualizar: number = 0;
  newProducto: any = {
    nombre: '',
    descripcion: '',
    precio: 0,
    tipo_insumo: '',
    duracion_insumo: '',
  };
  productos: any[] = [];

  constructor(
    private menuService: MenuService,
    private recetaService: RecetaService,
    private pedidoService: PedidoServices,
    private detallePedidoService: DetallePedidoService,
    private productoService: ProductoServices,
    private authService: AuthService
  ) {}

  logout() {
    this.authService.logout();
  }
  ngOnInit() {
    this.getMenus();
    this.getRecetas();
    this.getPedidos();
    this.getDetallePedidos();
    this.getProductos();
  }

  // Menús
  getMenus() {
    this.menuService.getMenus().subscribe(
      (response) => {
        this.menus = response;
      },
      (err) => console.error('Error al obtener los menús:', err)
    );
  }

  // Recetas
  getRecetas() {
    this.recetaService.getRecetas().subscribe(
      (response) => {
        this.recetas = response;
      },
      (err) => console.error('Error al obtener las recetas:', err)
    );
  }

  crearReceta() {
    this.recetaService.createReceta(this.newReceta).subscribe(
      (res) => {
        this.getRecetas();
        this.newReceta = {
          cantidad: 0,
          descripcion: '',
          producto_id: 0,
          unidadMedida: '',
          menu_id: 0,
          cantidadDisponible: 0,
        };
      },
      (err) => console.error('Error al crear la receta:', err)
    );
  }

  eliminarReceta() {
    if (!this.recetaIdAEliminar) return;
    this.recetaService.deleteReceta(this.recetaIdAEliminar).subscribe(
      (res) => {
        this.getRecetas();
      },
      (err) => console.error('Error al eliminar receta:', err)
    );
  }

  actualizarReceta() {
    if (!this.recetaIdAActualizar) return;
    this.recetaService
      .updateReceta(this.recetaIdAActualizar, this.newReceta)
      .subscribe(
        (res) => {
          this.getRecetas();
        },
        (err) => console.error('Error al actualizar receta:', err)
      );
  }

  // Pedidos
  getPedidos() {
    this.pedidoService.getPedidos().subscribe(
      (response) => {
        this.pedidos = response;
      },
      (err) => console.error('Error al obtener los pedidos:', err)
    );
  }

  crearPedido() {
    this.pedidoService.createPedido(this.newPedido).subscribe(
      (res) => {
        this.getPedidos();
        this.newPedido = { cliente_id: 0, estado_pedido: '', fecha_pedido: '' };
      },
      (err) => console.error('Error al crear el pedido:', err)
    );
  }

  eliminarPedido() {
    if (!this.pedidoIdAEliminar) return;
    this.pedidoService.deletePedido(this.pedidoIdAEliminar).subscribe(
      (res) => {
        this.getPedidos();
      },
      (err) => console.error('Error al eliminar el pedido:', err)
    );
  }

  // Detalle de pedidos
  getDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (response) => {
        this.detallePedidos = response;
      },
      (err) => console.error('Error al obtener los detalles de pedido:', err)
    );
  }

  crearDetallePedido() {
    this.detallePedidoService
      .createDetallePedido(this.newDetallePedido)
      .subscribe(
        (res) => {
          this.getDetallePedidos();
          this.newDetallePedido = { cantidad: 0, menu_id: 0, pedido_id: 0 };
        },
        (err) => console.error('Error al crear el detalle de pedido:', err)
      );
  }

  eliminarDetallePedido() {
    if (!this.detallePedidoIdAEliminar) return;
    this.detallePedidoService
      .deleteDetallePedido(this.detallePedidoIdAEliminar)
      .subscribe(
        (res) => {
          this.getDetallePedidos();
        },
        (err) => console.error('Error al eliminar el detalle de pedido:', err)
      );
  }

  actualizarDetallePedido() {
    if (!this.detallePedidoIdAActualizar) return;
    this.detallePedidoService
      .updateDetallePedido(
        this.detallePedidoIdAActualizar,
        this.newDetallePedido
      )
      .subscribe(
        (res) => {
          this.getDetallePedidos();
        },
        (err) => console.error('Error al actualizar el detalle de pedido:', err)
      );
  }

  // ------------------- PRODUCTOS ------------------- //

  getProductos() {
    this.productoService.getProductos().subscribe(
      (response) => {
        this.productos = response;
      },
      (err) => console.error('Error al obtener productos:', err)
    );
  }

  crearProducto() {
    this.productoService.crearProducto(this.newProducto).subscribe(
      (res) => {
        this.getProductos();
        this.newProducto = {
          nombre: '',
          descripcion: '',
          precio: 0,
          tipo_insumo: '',
          duracion_insumo: '',
        };
      },
      (err) => console.error('Error al crear producto:', err)
    );
  }

  eliminarProducto() {
    if (!this.productoIdAEliminar) return;
    this.productoService.eliminarProducto(this.productoIdAEliminar).subscribe(
      (res) => {
        this.getProductos();
      },
      (err) => console.error('Error al eliminar producto:', err)
    );
  }

  actualizarProducto() {
    if (!this.productoIdAActualizar) return;
    this.productoService
      .editarProducto(this.productoIdAActualizar, this.newProducto)
      .subscribe(
        (res) => {
          this.getProductos();
        },
        (err) => console.error('Error al actualizar producto:', err)
      );
  }

  buscarProductoPorNombre(nombre: string) {
    this.productoService.getProductoPorNombre(nombre).subscribe(
      (res) => {
        console.log('Producto encontrado:', res);
      },
      (err) => console.error('Error al buscar producto por nombre:', err)
    );
  }

  buscarProductosPorTipo(tipo: string) {
    this.productoService.getProductosPorTipo(tipo).subscribe(
      (res) => {
        console.log('Productos por tipo:', res);
      },
      (err) => console.error('Error al buscar productos por tipo:', err)
    );
  }

  buscarProductosPorPrecio(precio_min: number, precio_max: number) {
    this.productoService
      .getProductosPorPrecio(precio_min, precio_max)
      .subscribe(
        (res) => {
          console.log('Productos por precio:', res);
        },
        (err) => console.error('Error al buscar productos por precio:', err)
      );
  }
}
