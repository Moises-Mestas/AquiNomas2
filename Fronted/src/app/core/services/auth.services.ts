import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { resources } from '../resources/resources';

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient) {}

  login(userName: string, password: string): Observable<{ token: string }> {
    return this.http.post<{ token: string }>(resources.authLogin, {
      userName,
      password,
    });
  }

  register(userData: any): Observable<any> {
    return this.http.post(resources.authRegister, userData);
  }

  logout(): void {
    localStorage.removeItem('access_token');
    window.location.href = '/login';
  }
}
