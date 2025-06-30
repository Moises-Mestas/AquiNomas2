import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ReporteService {
  constructor(private http: HttpClient) {}

  // Reporte: Productos más rentables
  getProductosMasRentables(): Observable<any[]> {
    return this.http.get<any[]>(resources.productosMasRentables);
  }
}
