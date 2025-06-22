import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecetaService } from '../../core/services/receta.service';
import { MenuService } from '../../core/services/menu.service';
import { Receta } from '../../core/resources/receta.model';
import { Menu } from '../../core/resources/menu.model';

@Component({
  standalone: true,
  selector: 'app-receta',
  templateUrl: './receta.page.html',
  styleUrls: ['./receta.page.scss'],
  imports: [CommonModule, FormsModule]
})
export class RecetaPage implements OnInit {
  recetas: Receta[] = [];
  menus: Menu[] = [];
  formReceta: Receta = {
    productoId: 0,
    descripcion: '',
    cantidad: 0,
    unidadMedida: '',
    menu: {} as Menu,
    cantidadDisponible: 0
  };
  editing: boolean = false;

  constructor(
    private recetaService: RecetaService,
    private menuService: MenuService
  ) {}

  ngOnInit() {
    this.loadRecetas();
    this.menuService.getMenus().subscribe(data => this.menus = data);
  }

  loadRecetas() {
    this.recetaService.getRecetas().subscribe(data => this.recetas = data);
  }

  startEdit(receta: Receta) {
    this.formReceta = { ...receta, menu: receta.menu };
    this.editing = true;
  }

  saveReceta() {
    if (this.editing && this.formReceta.id) {
      this.recetaService.updateReceta(this.formReceta.id, this.formReceta).subscribe(() => {
        this.loadRecetas();
        this.cancel();
      });
    } else {
      this.recetaService.createReceta(this.formReceta).subscribe(() => {
        this.loadRecetas();
        this.cancel();
      });
    }
  }

  deleteReceta(id: number) {
    if (confirm('¿Estás seguro de eliminar esta receta?')) {
      this.recetaService.deleteReceta(id).subscribe(() => this.loadRecetas());
    }
  }

  cancel() {
    this.formReceta = {
      productoId: 0,
      descripcion: '',
      cantidad: 0,
      unidadMedida: '',
      menu: {} as Menu,
      cantidadDisponible: 0
    };
    this.editing = false;
  }
}
