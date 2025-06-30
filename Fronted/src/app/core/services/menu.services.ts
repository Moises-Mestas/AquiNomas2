import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class MenuService {
  constructor(private http: HttpClient) {}

  getMenus(): Observable<any[]> {
    return this.http.get<any[]>(resources.menus);
  }

  createMenu(menu: FormData): Observable<any> {
    return this.http.post<any>(resources.menus, menu);
  }

  updateMenu(id: number, menu: FormData): Observable<any> {
    return this.http.put<any>(`${resources.menus}/${id}`, menu);
  }

  deleteMenu(id: number): Observable<any> {
    return this.http.delete(`${resources.menus}/${id}`);
  }

  filterMenusByPriceRange(minPrice: number, maxPrice: number): Observable<any[]> {
    return this.http.get<any[]>(`${resources.menus}/filterByPriceRange?minPrecio=${minPrice}&maxPrecio=${maxPrice}`);
  }


}
