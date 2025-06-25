import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({
  providedIn: 'root',
})
export class RecetaService {
  private baseUrl = resources.recetas;

  constructor(private http: HttpClient) {}

  // GET: Obtener todas las recetas
  getRecetas(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // GET: Obtener una receta por su ID
  getRecetaById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  // POST: Crear una nueva receta
  crearReceta(receta: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, receta);
  }

  // PUT: Actualizar una receta por su ID
  actualizarReceta(id: number, receta: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, receta);
  }

  // DELETE: Eliminar una receta por su ID
  eliminarReceta(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
}
