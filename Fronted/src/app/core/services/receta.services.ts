import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';  // Asegúrate de que `resources` esté correctamente importado

@Injectable({ providedIn: 'root' })
export class RecetaService {
  constructor(private http: HttpClient) {}

  // Obtener todas las recetas
  getRecetas(): Observable<any[]> {
    return this.http.get<any[]>(resources.recetas);  // Reemplaza con la URL correcta de tu API
  }

  // Crear una nueva receta
  createReceta(receta: any): Observable<any> {
    return this.http.post<any>(resources.recetas, receta);
  }

  // Actualizar una receta existente
  updateReceta(id: number, receta: any): Observable<any> {
    return this.http.put<any>(`${resources.recetas}/${id}`, receta);
  }

  // Eliminar una receta por ID
  deleteReceta(id: number): Observable<any> {
    return this.http.delete(`${resources.recetas}/${id}`);
  }
}
