import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class VentaService {
  constructor(private http: HttpClient) {}

  getVentas(): Observable<any[]> {
    return this.http.get<any[]>(resources.ventas);
  }

  getVentaById(id: number): Observable<any> {
    return this.http.get(`${resources.ventas}/${id}`);
  }

  crearVenta(venta: any): Observable<any> {
    return this.http.post(resources.ventas, venta);
  }

  actualizarVenta(id: number, venta: any): Observable<any> {
    return this.http.put(`${resources.ventas}/${id}`, venta);
  }

  eliminarVenta(id: number): Observable<void> {
    return this.http.delete<void>(`${resources.ventas}/${id}`);
  }

  getPromocionesAplicables(pedidoId: number): Observable<any> {
    return this.http.get(`${resources.ventas}/promociones-aplicables/${pedidoId}`);
  }

  getVentasPorPromocion(promocionId: number): Observable<any[]> {
    return this.http.get<any[]>(`${resources.ventas}/promocion/${promocionId}`);
  }

  getVentasPorFecha(inicio: string, fin: string): Observable<any[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    return this.http.get<any[]>(`${resources.ventas}/fecha`, { params });
  }

  getVentasPorMetodoPago(metodo: string): Observable<any[]> {
    const params = new HttpParams().set('metodo', metodo);
    return this.http.get<any[]>(`${resources.ventas}/metodo-pago`, { params });
  }

  getPedidosPorNombreCliente(nombre: string): Observable<any[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<any[]>(`${resources.ventas}/pedidos/cliente`, { params });
  }

  getPedidosNoVendidosPorCliente(clienteId: number): Observable<any[]> {
    const params = new HttpParams().set('clienteId', clienteId.toString());
    return this.http.get<any[]>(`${resources.ventas}/pedidos/no-vendidos`, { params });
  }
}
