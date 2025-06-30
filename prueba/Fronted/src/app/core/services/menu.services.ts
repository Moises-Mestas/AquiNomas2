import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class MenuService {
  constructor(private http: HttpClient) {}

  // Obtener todos los menús
  getMenus(): Observable<any[]> {
    return this.http.get<any[]>(resources.menus); // Reemplaza con la URL correcta de tu API
  }


  // Crear un nuevo menú
  createMenu(menu: any): Observable<any> {
    return this.http.post<any>(resources.menus, menu);
  }

  // Actualizar un menú existente
  updateMenu(id: number, menu: any): Observable<any> {
    return this.http.put<any>(`${resources.menus}/${id}`, menu);
  }

  // Eliminar un menú por ID
  deleteMenu(id: number): Observable<any> {
    return this.http.delete(`${resources.menus}/${id}`);
  }
}
