import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecetaService } from '../../core/services/receta.services';

@Component({
  selector: 'app-receta',
  standalone: true,
  templateUrl: './receta.page.html',
  imports: [CommonModule, FormsModule],  // Agregar CommonModule para evitar errores en Angular
  styleUrls: ['./receta.page.css']
})
export class RecetaPage {
  recetas: any[] = [];
  newReceta: any = { cantidad: 0, descripcion: '', producto_id: 0, unidadMedida: '', menu_id: 0, cantidadDisponible: 0 };
  idAActualizar: number = 0;
  editing: boolean = false;

  constructor(private recetaService: RecetaService) {}

  ngOnInit() {
    this.getRecetas();
  }

  // Obtener todas las recetas
  getRecetas() {
    this.recetaService.getRecetas().subscribe(
      (response) => {
        this.recetas = response;
        this.recetas.sort((a, b) => b.id - a.id);

        console.log(this.recetas); // Verifica los datos en la consola
      },
      (err) => console.error('Error al obtener las recetas:', err)
    );
  }

  // Guardar receta (ya sea para crear o actualizar)
  saveReceta() {
    console.log('Datos a enviar:', this.newReceta);  // Verificar los datos antes de enviar

    if (this.idAActualizar) {
      this.updateReceta(); // Si hay un id, actualiza la receta
    } else {
      this.createReceta(); // Si no hay id, crea una nueva receta
    }
  }

  // Crear receta
  createReceta() {
    console.log("Datos a crear:", this.newReceta);

    if (!this.newReceta.producto_id || !this.newReceta.menu_id || !this.newReceta.cantidad || !this.newReceta.descripcion) {
      console.error('Datos faltantes:', this.newReceta);
      alert('Faltan campos requeridos.');
      return;
    }

    // Crear objeto Producto y Menu
    const producto = { id: this.newReceta.producto_id };
    const menu = { id: this.newReceta.menu_id };

    // Enviar objeto completo de la receta
    const receta = {
      productoId: this.newReceta.producto_id,
      descripcion: this.newReceta.descripcion,
      cantidad: this.newReceta.cantidad,
      unidadMedida: this.newReceta.unidadMedida,
      menu: menu, // Relacionamos el objeto del menú
      cantidadDisponible: this.newReceta.cantidadDisponible
    };

    this.recetaService.createReceta(receta).subscribe(
      (res) => {
        console.log('Receta creada:', res);
        this.getRecetas();
        this.clearForm();
      },
      (err) => {
        console.error('Error al crear receta:', err);
        alert('Hubo un error al crear la receta');
      }
    );
  }


  // Actualizar receta
  updateReceta() {
    console.log("Datos a actualizar:", this.newReceta);  // Verificar los datos antes de enviar

    if (!this.idAActualizar) {
      console.error('No se especificó el ID de la receta');
      return;
    }

    // Verifica que los campos requeridos estén completos
    if (!this.newReceta.producto_id || !this.newReceta.menu_id || !this.newReceta.cantidad || !this.newReceta.descripcion) {
      console.error('Datos faltantes:', this.newReceta);
      alert('Faltan campos requeridos.');
      return;
    }

    // Crear el objeto de Producto y Menu
    const producto = { id: this.newReceta.producto_id };
    const menu = { id: this.newReceta.menu_id };

    // Crear el objeto receta para enviar con los datos completos
    const receta = {
      id: this.idAActualizar,  // ID para actualizar la receta
      productoId: this.newReceta.producto_id,
      descripcion: this.newReceta.descripcion,
      cantidad: this.newReceta.cantidad,
      unidadMedida: this.newReceta.unidadMedida,
      menu: menu,  // Relación con el menú, con el objeto completo
      cantidadDisponible: this.newReceta.cantidadDisponible
    };

    this.recetaService.updateReceta(this.idAActualizar, receta).subscribe(
      (res) => {
        console.log('Receta actualizada:', res);
        this.getRecetas();  // Actualiza la lista de recetas
        this.clearForm();  // Limpiar formulario
      },
      (err) => {
        console.error('Error al actualizar receta:', err);
        alert('Hubo un error al actualizar la receta');
      }
    );
  }


  // Eliminar receta
  deleteReceta(id: number) {
    this.recetaService.deleteReceta(id).subscribe(
      (res) => {
        console.log('Receta eliminada:', res);
        this.getRecetas();  // Refrescar la lista de recetas
      },
      (err) => console.error('Error al eliminar receta:', err)
    );
  }

  // Editar receta (para carga de datos al formulario)
  editReceta(receta: any) {
    this.idAActualizar = receta.id;
    console.log('Receta a editar:', receta);  // Verifica los valores que se están pasando a newReceta

    // Asignamos los valores del objeto receta a newReceta
    this.newReceta = {
      id: receta.id,
      cantidad: receta.cantidad,
      descripcion: receta.descripcion,
      producto_id: receta.producto.id,  // Asegúrate de que receta.producto.id tenga valor
      unidadMedida: receta.unidadMedida,
      menu_id: receta.menu.id,  // Asegúrate de que receta.menu.id tenga valor
      cantidadDisponible: receta.cantidadDisponible
    };

    console.log('Datos a editar en el formulario:', this.newReceta);  // Verifica que los datos estén correctamente asignados
    this.editing = true;
  }

  // Limpiar formulario
  clearForm() {
    this.newReceta = { cantidad: 0, descripcion: '', producto_id: 0, unidadMedida: '', menu_id: 0, cantidadDisponible: 0 };
    this.idAActualizar = 0;
    this.editing = false;
  }
}
