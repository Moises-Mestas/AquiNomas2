import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources'; // Asegúrate de tener 'comprobantes'

@Injectable({ providedIn: 'root' })
export class ComprobanteService {
  constructor(private http: HttpClient) {}

  generarComprobante(ventaId: number, tipo: string): Observable<any> {
    const params = new HttpParams().set('tipo', tipo);
    return this.http.post(`${resources.comprobantes}/venta/${ventaId}`, null, { params });
  }

  descargarPDF(id: number): Observable<Blob> {
    return this.http.get(`${resources.comprobantes}/${id}/descargar`, {
      responseType: 'blob',
      headers: new HttpHeaders({ 'Accept': 'application/pdf' })
    });
  }

  listarComprobantes(): Observable<any[]> {
    return this.http.get<any[]>(resources.comprobantes);
  }

  obtenerComprobante(id: number): Observable<any> {
    return this.http.get(`${resources.comprobantes}/${id}`);
  }

  filtrarComprobante(tipo?: string, fechaInicio?: string, fechaFin?: string): Observable<any[]> {
    let params = new HttpParams();
    if (tipo) params = params.set('tipo', tipo);
    if (fechaInicio) params = params.set('fechaInicio', fechaInicio);
    if (fechaFin) params = params.set('fechaFin', fechaFin);
    return this.http.get<any[]>(`${resources.comprobantes}/filtrar`, { params });
  }

  eliminarComprobante(id: number): Observable<any> {
    return this.http.delete(`${resources.comprobantes}/${id}`);
  }
}
