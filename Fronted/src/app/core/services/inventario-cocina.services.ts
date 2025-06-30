import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class InventarioCocinaServices {
  constructor(private http: HttpClient) {}

  getInventarioCocina(): Observable<any[]> {
    return this.http.get<any[]>(resources.inventarioCocina);
  }

  crearInventarioCocinaDesdeBodega(data: any): Observable<any> {
    return this.http.post(resources.inventarioCocinaCrear, data);
  }

  getInventarioCocinaPorId(id: number): Observable<any> {
    return this.http.get(resources.inventarioCocinaPorId(id));
  }

  actualizarInventarioCocina(id: number, data: any): Observable<any> {
    return this.http.put(resources.inventarioCocinaPorId(id), data);
  }

  eliminarInventarioCocina(id: number): Observable<any> {
    return this.http.delete(resources.inventarioCocinaPorId(id));
  }

  getAlertaStockMinimo(): Observable<any[]> {
    return this.http.get<any[]>(resources.inventarioCocinaAlertaStockMinimo);
  }
}
