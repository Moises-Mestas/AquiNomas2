import { Route } from '@angular/router';
import { MenuPage } from './pages/menu.page/menu.page';
import { RecetaPage } from './pages/receta.page/receta.page';
import { PedidoPage } from './pages/pedido.page/pedido.page';
import { DetallePedidoPage } from './pages/DetallePedido.page/DetallePedido.page';
import { ProductoPage } from './pages/producto.page/producto.page';
import { ProveedorPage } from './pages/proveedores.page/proveedores.page';
import { ComprasProveedoresPage } from './pages/comprasProveedores/compras-proveedores.page';

export const routes: Route[] = [
  { path: 'menus', component: MenuPage }, // Ruta para el CRUD de Menú
  { path: 'recetas', component: RecetaPage }, // Ruta para el CRUD de Recetas
  { path: 'pedidos', component: PedidoPage }, // Ruta para el CRUD de Recetas
  { path: 'detalle-pedidos', component: DetallePedidoPage }, // Ruta para el CRUD de Recetas
  { path: 'productos', component: ProductoPage },
  { path: 'proveedores', component: ProveedorPage },
  { path: 'comprasProveedores', component: ComprasProveedoresPage },
];
