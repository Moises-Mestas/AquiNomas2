import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReporteService {

  private baseUrl = 'http://localhost:9000/reportes';

  constructor(private http: HttpClient) {}

  getProductosMasRentables(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/productos-mas-rentables`);
  }

  getPlatosBebidas(): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/platos-bebidas`);
  }

}
