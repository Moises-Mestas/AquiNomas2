import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MenuService } from '../../core/services/menu.service';
import { Menu } from '../../core/resources/menu.model';

@Component({
  standalone: true,
  selector: 'app-menu',
  templateUrl: './menu.page.html',
  styleUrls: ['./menu.page.scss'],
  imports: [CommonModule, FormsModule]
})
export class MenuPage implements OnInit {
  menus: Menu[] = [];
  formMenu: Menu = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
  editing: boolean = false;

  constructor(private menuService: MenuService) {}

  ngOnInit() {
    this.loadMenus();
  }

  loadMenus() {
    this.menuService.getMenus().subscribe(data => this.menus = data);
  }

  startEdit(menu: Menu) {
    this.formMenu = { ...menu };
    this.editing = true;
  }

  saveMenu() {
    if (this.editing && this.formMenu.id) {
      this.menuService.updateMenu(this.formMenu.id, this.formMenu).subscribe(() => {
        this.loadMenus();
        this.cancel();
      });
    } else {
      this.menuService.createMenu(this.formMenu).subscribe(() => {
        this.loadMenus();
        this.cancel();
      });
    }
  }

  deleteMenu(id: number) {
    if (confirm('¿Estás seguro de eliminar este menú?')) {
      this.menuService.deleteMenu(id).subscribe(() => this.loadMenus());
    }
  }

  cancel() {
    this.formMenu = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
    this.editing = false;
  }
}
