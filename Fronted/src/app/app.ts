import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NgFor } from '@angular/common'; // <-- Importa NgFor
import { ApiService } from './core/services/api.services';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NgFor], // <-- Agrega NgFor aquí
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected title = 'Fronted';
  public productos: any[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getProducto().subscribe((response) => {
      this.productos = response;
    });
  }
}
