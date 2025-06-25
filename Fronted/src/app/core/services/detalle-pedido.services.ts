import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({
  providedIn: 'root',
})
export class DetallePedidoService {
  private baseUrl = resources.detalle_pedidos;

  constructor(private http: HttpClient) {}

  // GET: Obtener todos los detalles de los pedidos
  getDetallePedidos(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // GET: Obtener detalle de un pedido por su ID
  getDetallePedidoById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  // POST: Crear un nuevo detalle de pedido
  crearDetallePedido(detalle: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, detalle);
  }

  // PUT: Actualizar un detalle de pedido por su ID
  actualizarDetallePedido(id: number, detalle: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, detalle);
  }

  // DELETE: Eliminar un detalle de pedido por su ID
  eliminarDetallePedido(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
}
