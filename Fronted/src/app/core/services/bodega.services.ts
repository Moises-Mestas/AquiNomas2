import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class BodegaServices {
  constructor(private http: HttpClient) {}

  getBodega(): Observable<any[]> {
    return this.http.get<any[]>(resources.bodega);
  }

  crearBodega(data: any): Observable<any> {
    return this.http.post(resources.bodega, data);
  }

  getBodegaPorId(id: number): Observable<any> {
    return this.http.get(resources.bodegaPorId(id));
  }

  actualizarBodega(id: number, data: any): Observable<any> {
    return this.http.put(resources.bodegaPorId(id), data);
  }

  eliminarBodega(id: number): Observable<any> {
    return this.http.delete(resources.bodegaPorId(id));
  }

  getBodegaPorTipo(tipo: string): Observable<any[]> {
    return this.http.get<any[]>(resources.bodegaPorTipo(tipo));
  }

  getBodegaPorFecha(fecha: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${resources.bodegaPorFecha}?fecha_entrada=${fecha}`
    );
  }

  getBodegaPorRangoFecha(
    fecha_inicio: string,
    fecha_fin: string
  ): Observable<any[]> {
    return this.http.get<any[]>(
      `${resources.bodegaPorRangoFecha}?fecha_inicio=${fecha_inicio}&fecha_fin=${fecha_fin}`
    );
  }

  getBodegaPorProducto(id: number): Observable<any[]> {
    return this.http.get<any[]>(resources.bodegaPorProducto(id));
  }

  getBodegaStockTotal(id: number): Observable<any> {
    return this.http.get(resources.bodegaStockTotal(id));
  }

  getBodegaStockBajo(stock_minimo: number): Observable<any[]> {
    return this.http.get<any[]>(
      `${resources.bodegaStockBajo}?stock_minimo=${stock_minimo}`
    );
  }

  getBodegaHistorial(id: number): Observable<any[]> {
    return this.http.get<any[]>(resources.bodegaHistorial(id));
  }
}
