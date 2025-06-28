import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProductoServices } from '../../core/services/producto.services';

@Component({
  selector: 'app-producto',
  standalone: true,
  templateUrl: './productos.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./producto.page.css'],
})
export class ProductoPage {
  productos: any[] = [];
  productosOriginal: any[] = [];
  newProducto: any = {
    nombre: '',
    descripcion: '',
    precio: 0,
    tipo_insumo: '',
    duracion_insumo: '',
  };
  idAActualizar: number = 0;
  editing: boolean = false;
  productoIdAEliminar: number = 0;

  filtroNombre: string = '';
  filtroTipo: string = '';

  constructor(private productoService: ProductoServices) {}

  ngOnInit() {
    this.getProductos();
  }

  getProductos() {
    this.productoService.getProductos().subscribe(
      (response) => {
        this.productos = response;
        this.productosOriginal = response;
      },
      (err) => console.error('Error al obtener productos:', err)
    );
  }

  clearForm() {
    this.newProducto = {
      nombre: '',
      descripcion: '',
      precio: 0,
      tipo_insumo: '',
      duracion_insumo: '',
    };
    this.idAActualizar = 0;
    this.editing = false;
  }

  abrirModalProducto(edit: boolean, producto: any = null) {
    if (edit && producto) {
      this.editing = true;
      this.idAActualizar = producto.id;
      this.newProducto = {
        nombre: producto.nombre,
        descripcion: producto.descripcion,
        precio: producto.precio,
        tipo_insumo: producto.tipo_insumo,
        duracion_insumo: producto.duracion_insumo,
      };
    } else {
      this.clearForm();
    }
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalProducto')
      );
      modal.show();
    }, 0);
  }

  abrirModalEliminar(id: number) {
    this.productoIdAEliminar = id;
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalEliminar')
      );
      modal.show();
    }, 0);
  }

  cerrarModal(id: string) {
    const modalElement = document.getElementById(id);
    if (modalElement) {
      const modalInstance = (window as any).bootstrap.Modal.getInstance(
        modalElement
      );
      if (modalInstance) {
        modalInstance.hide();
      }
    }
  }

  saveProducto() {
    if (this.editing) {
      this.productoService
        .editarProducto(this.idAActualizar, this.newProducto)
        .subscribe(
          (res) => {
            this.getProductos();
            this.clearForm();
            this.cerrarModal('modalProducto');
          },
          (err) => console.error('Error al actualizar producto:', err)
        );
    } else {
      this.productoService.crearProducto(this.newProducto).subscribe(
        (res) => {
          this.getProductos();
          this.clearForm();
          this.cerrarModal('modalProducto');
        },
        (err) => console.error('Error al crear producto:', err)
      );
    }
  }

  confirmarEliminar() {
    this.deleteProducto(this.productoIdAEliminar);
    this.cerrarModal('modalEliminar');
  }

  deleteProducto(id: number) {
    this.productoService.eliminarProducto(id).subscribe(
      (res) => {
        this.getProductos();
      },
      (err) => console.error('Error al eliminar producto:', err)
    );
  }

  // Filtros
  filtrarPorNombre() {
    if (!this.filtroNombre && !this.filtroTipo) {
      this.getProductos();
      return;
    }
    let filtrados = this.productosOriginal;
    if (this.filtroNombre) {
      filtrados = filtrados.filter((p: any) =>
        p.nombre.toLowerCase().includes(this.filtroNombre.toLowerCase())
      );
    }
    if (this.filtroTipo) {
      filtrados = filtrados.filter(
        (p: any) => p.tipo_insumo === this.filtroTipo
      );
    }
    this.productos = filtrados;
  }

  filtrarPorTipo() {
    if (!this.filtroNombre && !this.filtroTipo) {
      this.getProductos();
      return;
    }
    let filtrados = this.productosOriginal;
    if (this.filtroNombre) {
      filtrados = filtrados.filter((p: any) =>
        p.nombre.toLowerCase().includes(this.filtroNombre.toLowerCase())
      );
    }
    if (this.filtroTipo) {
      filtrados = filtrados.filter(
        (p: any) => p.tipo_insumo === this.filtroTipo
      );
    }
    this.productos = filtrados;
  }

  limpiarFiltros() {
    this.filtroNombre = '';
    this.filtroTipo = '';
    this.getProductos();
  }
}
