import { Route } from '@angular/router';
import {MenuPage} from './pages/menu.page/menu.page';
import {RecetaPage} from './pages/receta.page/receta.page';

export const routes: Route[] = [
  { path: 'menus', component: MenuPage}, // Ruta para el CRUD de Menú
  { path: 'recetas', component: RecetaPage },  // Ruta para el CRUD de Recetas

];
