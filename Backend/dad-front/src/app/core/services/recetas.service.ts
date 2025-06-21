import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Receta {
  productoId: number;
  descripcion: string;
  cantidad: number;
  unidadMedida: string;
  cantidadDisponible: number;
  menu: { id: number };
}

@Injectable({
  providedIn: 'root'
})
export class RecetasService {
  private apiUrl = 'http://localhost:9000/recetas';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Receta[]> {
    return this.http.get<Receta[]>(this.apiUrl);
  }
}

