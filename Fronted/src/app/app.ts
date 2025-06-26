import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms'; // ✅ Importa FormsModule
import { ApiService } from './core/services/api.services';

@Component({
  selector: 'app-root',
  standalone: true, // ✅ Standalone component
  imports: [RouterOutlet, FormsModule], // ✅ Solo una vez, incluye FormsModule
  templateUrl: './app.html',
  styleUrl: './app.css',
})
export class App {
  protected title = 'Fronted';
  idAEliminar: number = 0; // ✅ Variable enlazada al input

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.apiService.getProductos().subscribe((response) => {
      console.log(response);
    });
  }

eliminarPorId() {
  console.log('Eliminando producto con ID:', this.idAEliminar); // 👈

  if (!this.idAEliminar) return;

  this.apiService.eliminarProducto(this.idAEliminar).subscribe(
    (res) => console.log('Producto eliminado:', res),
    (err) => console.error('Error al eliminar:', err)
  );
}

}
