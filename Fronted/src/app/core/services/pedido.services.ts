import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({
  providedIn: 'root',
})
export class PedidoService {
  private baseUrl = resources.pedidos;

  constructor(private http: HttpClient) {}

  // GET: Obtener todos los pedidos
  getPedidos(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // GET: Obtener un pedido por su ID
  getPedidoById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  // POST: Crear un nuevo pedido
  crearPedido(pedido: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, pedido);
  }

  // PUT: Actualizar un pedido por su ID
  actualizarPedido(id: number, pedido: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, pedido);
  }

  // DELETE: Eliminar un pedido por su ID
  eliminarPedido(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
}
