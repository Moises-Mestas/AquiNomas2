import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MenuService } from '../../core/services/menu.services';
import { CurrencyPipe } from '@angular/common';
import {ImageService} from '../../core/services/image.service';

@Component({
  selector: 'app-menu',
  standalone: true,
  templateUrl: './menu.page.html',
  imports: [CommonModule, FormsModule, CurrencyPipe],
  styleUrls: ['./menu.page.css']

})
export class MenuPage {
  menus: any[] = [];
  filteredMenus: any[] = [];
  newMenu: any = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
  idAActualizar: number = 0;
  editing: boolean = false;
  searchText: string = '';
  showModal: boolean = false;
  selectedFile: File | null = null;
  uploadedImageUrl: string | null = null; // URL de la imagen cargada

  constructor(
    private menuService: MenuService,
    private imageService: ImageService
  ) {}
  ngOnInit() {
    this.getMenus();
  }

  getMenus() {
    this.menuService.getMenus().subscribe(
      (response) => {
        this.menus = response;
        this.filteredMenus = response;
        console.log(this.menus);
      },
      (err) => console.error('Error fetching menus:', err)
    );
  }

  searchMenu() {
    if (this.searchText.trim() === '') {
      this.filteredMenus = this.menus;
    } else {
      this.filteredMenus = this.menus.filter((menu) =>
        menu.nombre.toLowerCase().includes(this.searchText.toLowerCase())
      );
    }
  }

  openCreateMenuModal() {
    this.showModal = true;
    this.clearForm();
  }

  closeModal() {
    this.showModal = false;
  }

  saveMenu() {
    const formData = new FormData();
    formData.append('nombre', this.newMenu.nombre);
    formData.append('descripcion', this.newMenu.descripcion);
    formData.append('precio', this.newMenu.precio.toString());
    formData.append('tipo', this.newMenu.tipo);

    if (this.selectedFile) {
      formData.append('imagen', this.selectedFile, this.selectedFile.name); // Aquí solo enviamos el nombre del archivo
    } else if (this.newMenu.imagen) {
      // Si estamos editando y no seleccionamos una nueva imagen, enviamos la imagen actual
      formData.append('imagen', this.newMenu.imagen);
    }

    if (this.idAActualizar) {
      this.updateMenu(formData);
    } else {
      this.createMenu(formData);
    }
  }


  createMenu(menuData: FormData) {
    this.menuService.createMenu(menuData).subscribe(
      (res) => {
        console.log('Nuevo menú creado:', res);
        this.getMenus();
        this.clearForm();
        this.closeModal();
      },
      (err) => console.error('Error creating menu:', err)
    );
  }

  updateMenu(menuData: any) {
    this.menuService.updateMenu(this.idAActualizar, menuData).subscribe(
      (res) => {
        console.log('Menú actualizado:', res);
        this.getMenus();
        this.clearForm();
        this.closeModal();
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
    this.showModal = true;
  }

  onFileSelect(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.uploadedImageUrl = e.target.result; // Muestra la vista previa de la imagen seleccionada
      };
      reader.readAsDataURL(file);
    }
  }

  onUpload() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('image', this.selectedFile);

      this.imageService.uploadImage(formData).subscribe(
        (response) => {
          console.log('Imagen subida exitosamente', response);
          this.uploadedImageUrl = response.imageUrl; // URL de la imagen subida (suponiendo que el backend devuelva la URL)
        },
        (error) => {
          console.error('Error subiendo la imagen', error);
        }
      );
    } else {
      console.error('No hay archivo seleccionado');
    }
  }


  clearForm() {
    this.newMenu = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
    this.idAActualizar = 0;
    this.editing = false;
  }

  getImageUrl(imagePath: string): string {
    return `http://localhost:9000/public/${imagePath}`;  // URL correcta para acceder a las imágenes.
  }



}



