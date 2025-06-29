import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources'; // Asegúrate de tener `clientes` definido

@Injectable({ providedIn: 'root' })
export class ClienteService {
  constructor(private http: HttpClient) {}

  getClientes(): Observable<any[]> {
    return this.http.get<any[]>(resources.clientes);
  }

  getClienteById(id: number): Observable<any> {
    return this.http.get(`${resources.clientes}/${id}`);
  }

  crear(cliente: any): Observable<any> {
    return this.http.post(resources.clientes, cliente);
  }

  actualizar(cliente: any): Observable<any> {
    return this.http.put(resources.clientes, cliente);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${resources.clientes}/${id}`);
  }

  buscarPorNombre(nombre: string): Observable<any[]> {
    const params = new HttpParams().set('nombre', nombre);
    return this.http.get<any[]>(`${resources.clientes}/buscar-nombre`, { params });
  }

  buscarPorDni(dni: string): Observable<any> {
    const params = new HttpParams().set('dni', dni);
    return this.http.get(`${resources.clientes}/buscar-dni`, { params });
  }

  listarRecientes(dias: number = 30): Observable<any[]> {
    const params = new HttpParams().set('dias', dias);
    return this.http.get<any[]>(`${resources.clientes}/recientes`, { params });
  }
}
