import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  getProductos(): Observable<any[]> {
    return this.http.get<any[]>(resources.productos); // Se agregará la baseUrl por el interceptor
  }

  eliminarProducto(id: number): Observable<any> {
    return this.http.delete(`${resources.productos}/${id}`);
  }
}
