import {Route, RouterModule} from '@angular/router';
import { MenuPage } from './pages/menu.page/menu.page';
import { RecetaPage } from './pages/receta.page/receta.page';
import { PedidoPage } from './pages/pedido.page/pedido.page';
import { DetallePedidoPage } from './pages/DetallePedido.page/DetallePedido.page';
import { ProductoPage } from './pages/producto.page/producto.page';
import { ProveedorPage } from './pages/proveedores.page/proveedores.page';
import { ComprasProveedoresPage } from './pages/comprasProveedores/compras-proveedores.page';
import { BodegaPage } from './pages/bodega.page/bodega.page';
import { InventarioBarraPage } from './pages/barra.page/inventario-barra.page';
import { InventarioCocinaPage } from './pages/cocina.page/inventario-cocina.page';
import { AuthPage } from './pages/auth.page/auth.page';
import { ClientePage } from './pages/cliente.page/cliente.page';
import { RealizarPedidoPage } from './pages/realizarPedido.page/realizarPedido.page';
import { PedidoCompletoPage } from './pages/pedidoCompleto.page/pedidoCompleto.page';
import {NgModule} from '@angular/core';

export const routes: Route[] = [
  { path: 'menus', component: MenuPage }, // Ruta para el CRUD de Menú
  { path: 'recetas', component: RecetaPage }, // Ruta para el CRUD de Recetas
  { path: 'pedidos', component: PedidoPage }, // Ruta para el CRUD de Recetas
  { path: 'detalle-pedidos', component: DetallePedidoPage }, // Ruta para el CRUD de Recetas
  { path: 'clientes', component: ClientePage }, // Ruta para el CRUD de Recetas
  { path: 'realizar-pedido', component: RealizarPedidoPage }, // Nueva ruta
  { path: 'pedidoCompleto', component: PedidoCompletoPage },

  { path: 'productos', component: ProductoPage },
  { path: 'proveedores', component: ProveedorPage },
  { path: 'comprasProveedores', component: ComprasProveedoresPage },
  { path: 'bodega', component: BodegaPage },
  { path: 'barra', component: InventarioBarraPage },
  { path: 'cocina', component: InventarioCocinaPage },
  { path: 'login', component: AuthPage },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
