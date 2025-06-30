import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class InventarioBarraServices {
  constructor(private http: HttpClient) {}

  getInventarioBarra(): Observable<any[]> {
    return this.http.get<any[]>(resources.inventarioBarra);
  }

  crearInventarioBarraDesdeBodega(data: any): Observable<any> {
    return this.http.post(resources.inventarioBarraDesdeBodega, data);
  }

  getInventarioBarraPorId(id: number): Observable<any> {
    return this.http.get(resources.inventarioBarraPorId(id));
  }

  eliminarInventarioBarra(id: number): Observable<any> {
    return this.http.delete(resources.inventarioBarraPorId(id));
  }

  getAlertaStockMinimo(): Observable<any[]> {
    return this.http.get<any[]>(resources.inventarioBarraAlertaStockMinimo);
  }
}
