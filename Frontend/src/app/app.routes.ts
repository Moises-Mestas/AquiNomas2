import { Routes } from '@angular/router';
import { ReportesPage } from './pages/reportes.page/reportes.page';
import { ClientePage } from './pages/cliente.page/cliente.page';

export const routes: Routes = [
  { path: 'reportes', component: ReportesPage },
  { path: 'clientes', component: ClientePage }
];
