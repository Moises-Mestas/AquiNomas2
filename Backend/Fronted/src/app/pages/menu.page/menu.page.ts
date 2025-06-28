import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MenuService } from '../../core/services/menu.services';
import { CurrencyPipe } from '@angular/common';

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
  uploadStatus: { success: boolean, message: string } | null = null;

  constructor(private menuService: MenuService) {}

  ngOnInit() {
    this.getMenus();
    this.loadStoredImages();  // Cargar imágenes guardadas en localStorage
  }

  getMenus() {
    this.menuService.getMenus().subscribe(
      (response) => {
        this.menus = response;
        this.filteredMenus = response;
        this.loadStoredImages(); // Recargar las imágenes desde localStorage
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

    // Asegúrate de que la imagen sea parte del FormData si se seleccionó un archivo
    if (this.selectedFile) {
      formData.append('imagen', this.selectedFile, this.selectedFile.name);
    }

    if (this.idAActualizar) {
      this.updateMenu(formData);
    } else {
      this.createMenu(formData);  // Aquí se envía el FormData con la imagen
    }
  }

  createMenu(menuData: FormData) {
    this.menuService.createMenu(menuData).subscribe(
      (res) => {
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

  onFileSelected(event: any, menu: any) {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.readAsDataURL(file);  // Leer la imagen como base64
      reader.onload = () => {
        menu.imagen = reader.result as string;  // Asignar la imagen al menú
        menu.imagenSubida = true;  // Marcar que se ha subido una imagen
        localStorage.setItem(`menu-image-${menu.id}`, menu.imagen);  // Guardar la imagen en localStorage
      };
    }
  }

  changeImage(menu: any) {
    // Resetea la imagen para permitir al usuario subir una nueva
    menu.imagenSubida = false;
    menu.imagen = null;
    localStorage.removeItem(`menu-image-${menu.id}`);  // Eliminar imagen de localStorage
  }

  loadStoredImages() {
    // Cargar todas las imágenes almacenadas en localStorage al cargar la página
    this.menus.forEach(menu => {
      const storedImage = localStorage.getItem(`menu-image-${menu.id}`);
      if (storedImage) {
        menu.imagen = storedImage;
        menu.imagenSubida = true;
      }
    });
  }

  clearForm() {
    this.newMenu = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
    this.idAActualizar = 0;
    this.editing = false;
  }

  getSavedImage(menuId: number): string | null {
    return localStorage.getItem(`menu-image-${menuId}`);  // Recuperar imagen desde localStorage
  }
}
