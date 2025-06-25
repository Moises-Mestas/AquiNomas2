import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({
  providedIn: 'root',
})
export class MenuService {
  private baseUrl = resources.menus;

  constructor(private http: HttpClient) {}

  // GET: Obtener todos los menús
  getMenus(): Observable<any[]> {
    return this.http.get<any[]>(this.baseUrl);
  }

  // GET: Obtener un menú por su ID
  getMenuById(id: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/${id}`);
  }

  // POST: Crear un nuevo menú
  crearMenu(menu: any): Observable<any> {
    return this.http.post<any>(this.baseUrl, menu);
  }

  // PUT: Actualizar un menú por su ID
  actualizarMenu(id: number, menu: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/${id}`, menu);
  }

  // DELETE: Eliminar un menú por su ID
  eliminarMenu(id: number): Observable<any> {
    return this.http.delete<any>(`${this.baseUrl}/${id}`);
  }
}
