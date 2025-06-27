import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';  // Importa CommonModule
import { FormsModule } from '@angular/forms';
import { MenuService } from '../../core/services/menu.services';
import { CurrencyPipe } from '@angular/common';

@Component({
  selector: 'app-menu',
  standalone: true,
  templateUrl: './menu.page.html',
  imports: [CommonModule, FormsModule, CurrencyPipe],  // Agrega CommonModule
  styleUrls: ['./menu.page.css']
})
export class MenuPage {
  menus: any[] = [];
  newMenu: any = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
  idAActualizar: number = 0;
  editing: boolean = false;

  constructor(private menuService: MenuService) {}

  ngOnInit() {
    this.getMenus();
  }

  getMenus() {
    this.menuService.getMenus().subscribe(
      (response) => {
        this.menus = response;
        console.log(this.menus);
      },
      (err) => console.error('Error fetching menus:', err)
    );
  }

  saveMenu() {
    if (this.idAActualizar) {
      this.updateMenu();
    } else {
      this.createMenu();
    }
  }

  createMenu() {
    this.menuService.createMenu(this.newMenu).subscribe(
      (res) => {
        console.log('Nuevo menú creado:', res);
        this.getMenus();
        this.clearForm();
      },
      (err) => console.error('Error creating menu:', err)
    );
  }

  updateMenu() {
    this.menuService.updateMenu(this.idAActualizar, this.newMenu).subscribe(
      (res) => {
        console.log('Menú actualizado:', res);
        this.getMenus();
        this.clearForm();
      },
      (err) => console.error('Error updating menu:', err)
    );
  }

  deleteMenu(id: number) {
    this.menuService.deleteMenu(id).subscribe(
      (res) => {
        console.log('Menú eliminado:', res);
        this.getMenus();
      },
      (err) => console.error('Error deleting menu:', err)
    );
  }

  editMenu(menu: any) {
    this.idAActualizar = menu.id;
    this.newMenu = { ...menu };
    this.editing = true;
  }

  clearForm() {
    this.newMenu = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
    this.idAActualizar = 0;
    this.editing = false;
  }
}
