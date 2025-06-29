import { Route } from '@angular/router';
import {MenuPage} from './pages/menu.page/menu.page';
import {VentaPage} from './pages/venta.page/venta.page';
import {ComprobantePage} from './pages/comprobante.page/comprobante.page';
import {PromocionPage} from './pages/promocion.page/promocion.page';

export const routes: Route[] = [
  { path: 'menus', component: MenuPage}, // Ruta para el CRUD de Menú
  { path: 'ventas', component: VentaPage},
  { path: 'promociones', component: PromocionPage},
  { path: 'comprobantes', component: ComprobantePage},

  // Otras rutas de tu aplicación...
];
