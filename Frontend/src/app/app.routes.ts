import { Routes } from '@angular/router';
import { ReportesPage } from './pages/reportes.page/reportes.page';

export const routes: Routes = [
  { path: '', redirectTo: 'reportes', pathMatch: 'full' },
  { path: 'reportes', component: ReportesPage }
];
