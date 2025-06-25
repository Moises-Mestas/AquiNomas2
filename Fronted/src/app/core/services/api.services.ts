import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}

  getProducto(): Observable<any[]> {
    return this.http.get<any[]>(resources.catalogo.productos);
  }

  eliminarProducto(): Observable<any[]> {
    return this.http.delete<any[]>(resources.catalogo.productos);
  }

  crearUsuario(data: any): Observable<any> {
    return this.http.post('usuarios', data);
  }
}
