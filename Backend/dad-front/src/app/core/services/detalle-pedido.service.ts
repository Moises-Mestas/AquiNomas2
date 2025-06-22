import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DetallePedido } from '../resources/detalle-pedido.model';

@Injectable({ providedIn: 'root' })
export class DetallePedidoService {
  private apiUrl = 'http://localhost:9000/detalle-pedidos'; // Ajusta según tu backend

  constructor(private http: HttpClient) {}

  getDetalles(): Observable<DetallePedido[]> {
    return this.http.get<DetallePedido[]>(this.apiUrl);
  }

  getDetalle(id: number): Observable<DetallePedido> {
    return this.http.get<DetallePedido>(`${this.apiUrl}/${id}`);
  }

  createDetalle(detalle: DetallePedido): Observable<DetallePedido> {
    return this.http.post<DetallePedido>(this.apiUrl, detalle);
  }

  updateDetalle(id: number, detalle: DetallePedido): Observable<DetallePedido> {
    return this.http.put<DetallePedido>(`${this.apiUrl}/${id}`, detalle);
  }

  deleteDetalle(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
