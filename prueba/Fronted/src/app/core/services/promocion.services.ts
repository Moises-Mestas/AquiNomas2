import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources'; // Asegúrate que incluya 'promociones'

@Injectable({ providedIn: 'root' })
export class PromocionService {
  constructor(private http: HttpClient) {}

  crearPromocion(promocion: any): Observable<any> {
    return this.http.post(resources.promociones, promocion);
  }

  actualizarPromocion(id: number, promocion: any): Observable<any> {
    return this.http.put(`${resources.promociones}/${id}`, promocion);
  }

  obtenerPorIdPromocion(id: number): Observable<any> {
    return this.http.get(`${resources.promociones}/${id}`);
  }

  listarTodasPromociones(): Observable<any[]> {
    return this.http.get<any[]>(resources.promociones);
  }

  eliminarPromocion(id: number): Observable<void> {
    return this.http.delete<void>(`${resources.promociones}/${id}`);
  }

  buscarPorNombrePromocion(nombre: string): Observable<any[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<any[]>(`${resources.promociones}/buscar`, { params });
  }

  Promocionesactivas(fecha?: string): Observable<any[]> {
    const params = fecha ? new HttpParams().set('fecha', fecha) : undefined;
    return this.http.get<any[]>(`${resources.promociones}/activas`, { params });
  }

  PromocionestaActiva(id: number, fecha?: string): Observable<boolean> {
    const params = fecha ? new HttpParams().set('fecha', fecha) : undefined;
    return this.http.get<boolean>(`${resources.promociones}/${id}/activa`, { params });
  }

  PromocionconMinimo(): Observable<any[]> {
    return this.http.get<any[]>(`${resources.promociones}/con-cantidad-minima`);
  }

  porTipoPromociones(tipo: string): Observable<any[]> {
    const params = new HttpParams().set('tipo', tipo);
    return this.http.get<any[]>(`${resources.promociones}/por-tipo`, { params });
  }
}
