import { Route } from '@angular/router';
import {MenuPage} from './pages/menu.page/menu.page';
import {RecetaPage} from './pages/receta.page/receta.page';
import {PedidoPage} from './pages/pedido.page/pedido.page';
import {DetallePedidoPage} from './pages/DetallePedido.page/DetallePedido.page';
import {ClientePage} from './pages/cliente.page/cliente.page';
import {RealizarPedidoPage} from './pages/realizarPedido.page/realizarPedido.page';
import {PedidoCompletoPage} from './pages/pedidoCompleto.page/pedidoCompleto.page';
import {ReportesPage} from './pages/reportes.page/reportes.page';

export const routes: Route[] = [
  { path: 'menus', component: MenuPage}, // Ruta para el CRUD de Menú
  { path: 'recetas', component: RecetaPage },  // Ruta para el CRUD de Recetas
  { path: 'pedidos', component: PedidoPage },  // Ruta para el CRUD de Recetas
  { path: 'detalle-pedidos', component: DetallePedidoPage },  // Ruta para el CRUD de Recetas
  { path: 'clientes', component: ClientePage },  // Ruta para el CRUD de Recetas
  { path: 'realizar-pedido', component: RealizarPedidoPage },  // Nueva ruta
  { path: 'pedidoCompleto', component: PedidoCompletoPage },
  { path: 'reportes', component: ReportesPage },
];
