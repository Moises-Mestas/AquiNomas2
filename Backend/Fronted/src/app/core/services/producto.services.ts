import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources'; // Asegúrate de que la URL esté definida en este archivo

@Injectable({ providedIn: 'root' })
export class ProductoService {

  constructor(private http: HttpClient) {}

  // Obtener todos los productos
  getProductos(): Observable<any[]> {
    return this.http.get<any[]>(resources.productos); // URL de los productos (puedes modificarla si es necesario)
  }
}
