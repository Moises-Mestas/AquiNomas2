import { Route } from '@angular/router';
import {MenuPage} from './pages/menu.page/menu.page';

export const routes: Route[] = [
  { path: 'menus', component: MenuPage}, // Ruta para el CRUD de Menú
  // Otras rutas de tu aplicación...
];
