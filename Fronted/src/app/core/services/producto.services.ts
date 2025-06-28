import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ProductoServices {
  constructor(private http: HttpClient) {}

  getProductos(): Observable<any[]> {
    return this.http.get<any[]>(resources.productos);
  }

  crearProducto(data: any): Observable<any> {
    return this.http.post(resources.productos, data);
  }

  getProductoPorId(id: number): Observable<any> {
    return this.http.get(resources.productoPorId(id));
  }

  editarProducto(id: number, data: any): Observable<any> {
    return this.http.put(resources.productoPorId(id), data);
  }

  eliminarProducto(id: number): Observable<any> {
    return this.http.delete(resources.productoPorId(id));
  }

  getProductoPorNombre(nombre: string): Observable<any> {
    return this.http.get(resources.productoPorNombre(nombre));
  }

  getProductosPorTipo(tipo: string): Observable<any[]> {
    return this.http.get<any[]>(resources.productoPorTipo(tipo));
  }

  getProductosPorPrecio(
    precio_min: number,
    precio_max: number
  ): Observable<any[]> {
    return this.http.get<any[]>(
      `${resources.productoPorPrecio}?precio_min=${precio_min}&precio_max=${precio_max}`
    );
  }
}
