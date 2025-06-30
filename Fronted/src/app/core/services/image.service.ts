import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ImageService {

  constructor(private http: HttpClient) { }

  uploadImage(imageData: FormData): Observable<any> {
    return this.http.post('http://localhost:9000/upload', imageData); // Ajusta la URL según tu API
  }
}
