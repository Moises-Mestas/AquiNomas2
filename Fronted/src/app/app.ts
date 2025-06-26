import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms'; // ✅ Importa FormsModule
import { ApiService } from './core/services/api.services';
import { MenuService } from './core/services/menu.services';
import {MenuPage} from './pages/menu.page/menu.page'; // ✅ Importar MenuService

@Component({
  selector: 'app-root',
  standalone: true, // ✅ Standalone component
  imports: [RouterOutlet, FormsModule, MenuPage], // ✅ Incluye MenuPage en los imports
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
  protected title = 'Fronted';
  idAEliminar: number = 0; // ✅ Variable enlazada al input
  idAActualizar: number = 0; // ✅ ID para actualizar un menú
  newMenu: any = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' }; // Datos para el nuevo menú
  menus: any[] = []; // Lista de menús

  constructor(
    private apiService: ApiService,
    private menuService: MenuService // ✅ Inyectar MenuService
  ) {}

  ngOnInit() {
    // Obtener los menús al iniciar
    this.menuService.getMenus().subscribe((response) => {
      this.menus = response;
      console.log(this.menus);
    });
  }

  eliminarPorId() {
    console.log('Eliminando producto con ID:', this.idAEliminar);

    if (!this.idAEliminar) return;

    this.apiService.eliminarProducto(this.idAEliminar).subscribe(
      (res) => console.log('Producto eliminado:', res),
      (err) => console.error('Error al eliminar:', err)
    );
  }

  // Método para eliminar un menú
  eliminarMenu() {
    if (!this.idAEliminar) return;

    this.menuService.deleteMenu(this.idAEliminar).subscribe(
      (res) => {
        console.log('Menú eliminado:', res);
        this.menuService.getMenus().subscribe((menus) => {
          this.menus = menus;
        }); // Refrescar la lista de menús
      },
      (err) => console.error('Error al eliminar el menú:', err)
    );
  }

  // Método para crear un nuevo menú
  crearMenu() {
    this.menuService.createMenu(this.newMenu).subscribe(
      (res) => {
        console.log('Nuevo menú creado:', res);
        this.menuService.getMenus().subscribe((menus) => {
          this.menus = menus;
        }); // Refrescar la lista de menús
        this.newMenu = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' }; // Resetear formulario
      },
      (err) => console.error('Error al crear el menú:', err)
    );
  }

  // Método para actualizar un menú
  actualizarMenu() {
    if (!this.idAActualizar) return;

    this.menuService.updateMenu(this.idAActualizar, this.newMenu).subscribe(
      (res) => {
        console.log('Menú actualizado:', res);
        this.menuService.getMenus().subscribe((menus) => {
          this.menus = menus;
        }); // Refrescar la lista de menús
      },
      (err) => console.error('Error al actualizar el menú:', err)
    );
  }
}
