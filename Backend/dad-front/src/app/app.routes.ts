import { Routes } from '@angular/router';
import { Home } from './pages/home/home';
import {MenuPage} from './pages/menu.page/menu.page';

export const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: Home },
  { path: 'menus', component: MenuPage }

];
