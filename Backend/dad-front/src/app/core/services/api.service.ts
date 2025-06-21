import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class ApiService {
  constructor(private http: HttpClient) {}



  crearUsuario(data: any): Observable<any> {
    return this.http.post('usuarios', data);
  }
}
