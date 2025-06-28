import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({
  providedIn: 'root',
})
export class PedidoServices {
  constructor(private http: HttpClient) {}

  // Obtener todos los pedidos
  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(resources.pedidos);
  }

  // Crear un nuevo pedido
  createPedido(pedido: any): Observable<any> {
    return this.http.post<any>(resources.pedidos, pedido);
  }

  // Actualizar un pedido
  updatePedido(id: number, pedido: any): Observable<any> {
    return this.http.put<any>(`${resources.pedidos}/${id}`, pedido);
  }

  // Eliminar un pedido
  deletePedido(id: number): Observable<any> {
    return this.http.delete(`${resources.pedidos}/${id}`);
  }
}
