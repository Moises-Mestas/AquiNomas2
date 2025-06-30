import { Routes } from '@angular/router';
import { ReportesComponent } from './pages/reportes/reportes.component';
import { PlatosBebidasComponent } from './pages/platos-bebidas.component';

export const routes: Routes = [
  { path: '', redirectTo: 'reportes', pathMatch: 'full' },
  { path: 'reportes', component: ReportesComponent },
  {
  path: 'platos-bebidas',
  loadComponent: () =>
    import('./pages/platos-bebidas.component').then((m) => m.PlatosBebidasComponent),
}


];
