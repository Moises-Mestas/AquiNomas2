import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class ClienteService {
  constructor(private http: HttpClient) {}

  getClientes(): Observable<any[]> {
    return this.http.get<any[]>(resources.clientes);
  }

  createCliente(cliente: FormData): Observable<any> {
    return this.http.post<any>(resources.clientes, cliente);
  }

  updateCliente(id: number, cliente: FormData): Observable<any> {
    return this.http.put<any>(`${resources.clientes}/${id}`, cliente);
  }

  deleteCliente(id: number): Observable<any> {
    return this.http.delete(`${resources.clientes}/${id}`);
  }




}
