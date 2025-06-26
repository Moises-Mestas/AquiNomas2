import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from './core/services/api.services';
import { MenuService } from './core/services/menu.services';
import { RecetaService } from './core/services/receta.services';
import { MenuPage } from './pages/menu.page/menu.page';  // Solo importar cuando se use MenuPage
import { RecetaPage } from './pages/receta.page/receta.page';  // Solo importar cuando se use RecetaPage

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, CommonModule],  // Solo importar los módulos comunes
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
  protected title = 'Fronted';

  // Declaración de las variables para menús
  idAEliminar: number = 0;
  idAActualizar: number = 0;
  newMenu: any = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
  menus: any[] = [];  // Esta es la propiedad para almacenar los menús

  // Declaración de las variables para recetas
  recetaIdAEliminar: number = 0;
  recetaIdAActualizar: number = 0;
  newReceta: any = { cantidad: 0, descripcion: '', producto_id: 0, unidadMedida: '', menu_id: 0, cantidadDisponible: 0 };
  recetas: any[] = []; // Esta es la propiedad para almacenar las recetas

  constructor(
    private apiService: ApiService,
    private menuService: MenuService,
    private recetaService: RecetaService
  ) {}

  // Cargar los menús y las recetas
  ngOnInit() {
    this.getMenus(); // Cargar menús
    this.getRecetas(); // Cargar recetas
  }

  // Método para obtener todos los menús
  getMenus() {
    this.menuService.getMenus().subscribe(
      (response) => {
        this.menus = response; // Asigna los menús obtenidos a la propiedad 'menus'
        console.log(this.menus);
      },
      (err) => console.error('Error al obtener los menús:', err)
    );
  }

  // Método para obtener todas las recetas
  getRecetas() {
    this.recetaService.getRecetas().subscribe(
      (response) => {
        this.recetas = response; // Asigna las recetas obtenidas a la propiedad 'recetas'
        console.log(this.recetas);
      },
      (err) => console.error('Error al obtener las recetas:', err)
    );
  }

  // Crear una receta
  crearReceta() {
    this.recetaService.createReceta(this.newReceta).subscribe(
      (res) => {
        console.log('Receta creada:', res);
        this.getRecetas();  // Refresca la lista de recetas
        this.newReceta = { cantidad: 0, descripcion: '', producto_id: 0, unidadMedida: '', menu_id: 0, cantidadDisponible: 0 };  // Limpia el formulario
      },
      (err) => console.error('Error al crear la receta:', err)
    );
  }

  // Eliminar una receta
  eliminarReceta() {
    if (!this.recetaIdAEliminar) return;

    this.recetaService.deleteReceta(this.recetaIdAEliminar).subscribe(
      (res) => {
        console.log('Receta eliminada:', res);
        this.getRecetas();  // Refresca la lista
      },
      (err) => console.error('Error al eliminar receta:', err)
    );
  }

  // Actualizar una receta
  actualizarReceta() {
    if (!this.recetaIdAActualizar) return;

    this.recetaService.updateReceta(this.recetaIdAActualizar, this.newReceta).subscribe(
      (res) => {
        console.log('Receta actualizada:', res);
        this.getRecetas();  // Refresca la lista
      },
      (err) => console.error('Error al actualizar receta:', err)
    );
  }
}
