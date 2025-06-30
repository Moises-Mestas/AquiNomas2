import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ProveedorServices {
  constructor(private http: HttpClient) {}

  getProveedores(): Observable<any[]> {
    return this.http.get<any[]>(resources.proveedores);
  }

  crearProveedor(data: any): Observable<any> {
    return this.http.post(resources.proveedores, data);
  }

  getProveedorPorId(id: number): Observable<any> {
    return this.http.get(resources.proveedorPorId(id));
  }

  editarProveedor(id: number, data: any): Observable<any> {
    return this.http.put(resources.proveedorPorId(id), data);
  }

  eliminarProveedor(id: number): Observable<any> {
    return this.http.delete(resources.proveedorPorId(id));
  }

  getProveedoresPorEstado(estado: string): Observable<any[]> {
    return this.http.get<any[]>(resources.proveedorPorEstado(estado));
  }

  getProveedorPorNombre(nombre: string): Observable<any[]> {
    return this.http.get<any[]>(resources.proveedorPorNombre(nombre));
  }
}
