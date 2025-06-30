import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ComprasProveedoresServices {
  constructor(private http: HttpClient) {}

  getCompras(): Observable<any[]> {
    return this.http.get<any[]>(resources.comprasProveedores);
  }

  crearCompra(data: any): Observable<any> {
    return this.http.post(resources.comprasProveedores, data);
  }

  getCompraPorId(id: number): Observable<any> {
    return this.http.get(resources.compraProveedorPorId(id));
  }

  editarCompra(id: number, data: any): Observable<any> {
    return this.http.put(resources.compraProveedorPorId(id), data);
  }

  eliminarCompra(id: number): Observable<any> {
    return this.http.delete(resources.compraProveedorPorId(id));
  }

  getComprasPorRangoFecha(
    fecha_inicio: string,
    fecha_fin: string
  ): Observable<any[]> {
    return this.http.get<any[]>(
      `${resources.comprasPorFecha}?fecha_inicio=${fecha_inicio}&fecha_fin=${fecha_fin}`
    );
  }

  getComprasPorFecha(fecha: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${resources.comprasPorFechaEspecifica}?fecha=${fecha}`
    );
  }
}
