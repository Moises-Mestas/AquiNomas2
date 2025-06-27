import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';  // Importamos los recursos

@Injectable({
  providedIn: 'root',
})
export class DetallePedidoServices {
  constructor(private http: HttpClient) {}

  // Obtener todos los detalles del pedido
  getDetallePedidos(): Observable<any[]> {
    return this.http.get<any[]>(resources.detalle_pedidos);
  }

  // Crear un nuevo detalle de pedido
  createDetallePedido(detallePedido: any): Observable<any> {
    return this.http.post<any>(resources.detalle_pedidos, detallePedido);
  }

  // Actualizar un detalle de pedido existente
  updateDetallePedido(id: number, detallePedido: any): Observable<any> {
    return this.http.put<any>(`${resources.detalle_pedidos}/${id}`, detallePedido);
  }

  // Eliminar un detalle de pedido
  deleteDetallePedido(id: number): Observable<any> {
    return this.http.delete(`${resources.detalle_pedidos}/${id}`);
  }
}
