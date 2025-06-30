import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  constructor(private http: HttpClient) {}

  getProductosMasRentables(): Observable<any[]> {
    return this.http.get<any[]>(`${resources.reportes}/productos-mas-rentables`);
  }

  exportarProductosMasRentables(): Observable<Blob> {
    return this.http.get(`${resources.reportes}/productos-mas-rentables/pdf`, {
      responseType: 'blob'
    });
  }

  getPlatosBebidas(): Observable<any> {
    return this.http.get<any>(`${resources.reportes}/platos-bebidas`);
  }

  exportarPlatosBebidas(): Observable<Blob> {
    return this.http.get(`${resources.reportes}/pdf/platos-bebidas`, {
      responseType: 'blob'
    });
  }

  getTodosReportes(): Observable<any[]> {
    return this.http.get<any[]>(`${resources.reportes}`);
  }

  exportarTodosReportes(): Observable<Blob> {
    return this.http.get(`${resources.reportes}/pdf`, {
      responseType: 'blob'
    });
  }
}
