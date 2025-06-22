import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import {MenuPage} from './pages/menu.page/menu.page';
import {RecetaPage} from './pages/receta.page/receta.page';
import {PedidoPage} from './pages/pedido.page/pedido.page';
import {DetallePedidoPage} from './pages/detalle-pedido.page/detalle-pedido.page';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: Home },
  { path: 'menus', component: MenuPage },
  { path: 'recetas', component: RecetaPage },
  { path: 'pedidos', component: PedidoPage },
  { path: 'detalle-pedidos', component: DetallePedidoPage }


];
