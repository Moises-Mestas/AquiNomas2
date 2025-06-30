import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ProveedorServices } from '../../core/services/proveedores.services';
import { AuthService } from '../../core/services/auth.services';

@Component({
  selector: 'app-proveedor',
  standalone: true,
  templateUrl: './proveedores.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./proveedor.page.css'],
})
export class ProveedorPage {
  proveedores: any[] = [];
  proveedoresOriginal: any[] = [];
  newProveedor: any = {
    nombre: '',
    telefono: '',
    direccion: '',
    email: '',
    ruc: '',
    estado: 'activo',
  };
  idAActualizar: number = 0;
  editing: boolean = false;
  proveedorIdAEliminar: number = 0;

  // Filtros
  filtroNombre: string = '';
  filtroEstado: string = '';

  constructor(
    private proveedorService: ProveedorServices,
    private authService: AuthService
  ) {}

  logout() {
    this.authService.logout();
  }

  ngOnInit() {
    this.getProveedores();
  }

  getProveedores() {
    this.proveedorService.getProveedores().subscribe(
      (response) => {
        this.proveedores = response;
        this.proveedoresOriginal = response;
      },
      (err) => console.error('Error al obtener proveedores:', err)
    );
  }

  clearForm() {
    this.newProveedor = {
      nombre: '',
      telefono: '',
      direccion: '',
      email: '',
      ruc: '',
      estado: 'activo',
    };
    this.idAActualizar = 0;
    this.editing = false;
  }

  abrirModalProveedor(edit: boolean, proveedor: any = null) {
    if (edit && proveedor) {
      this.editing = true;
      this.idAActualizar = proveedor.id;
      this.newProveedor = {
        nombre: proveedor.nombre,
        telefono: proveedor.telefono,
        direccion: proveedor.direccion,
        email: proveedor.email,
        ruc: proveedor.ruc,
        estado: proveedor.estado,
      };
    } else {
      this.clearForm();
    }
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalProveedor')
      );
      modal.show();
    }, 0);
  }

  abrirModalEliminar(id: number) {
    this.proveedorIdAEliminar = id;
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalEliminarProveedor')
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

  saveProveedor() {
    if (this.editing) {
      this.proveedorService
        .editarProveedor(this.idAActualizar, this.newProveedor)
        .subscribe(
          (res) => {
            this.getProveedores();
            this.clearForm();
            this.cerrarModal('modalProveedor');
          },
          (err) => console.error('Error al actualizar proveedor:', err)
        );
    } else {
      this.proveedorService.crearProveedor(this.newProveedor).subscribe(
        (res) => {
          this.getProveedores();
          this.clearForm();
          this.cerrarModal('modalProveedor');
        },
        (err) => console.error('Error al crear proveedor:', err)
      );
    }
  }

  confirmarEliminar() {
    this.deleteProveedor(this.proveedorIdAEliminar);
    this.cerrarModal('modalEliminarProveedor');
  }

  deleteProveedor(id: number) {
    this.proveedorService.eliminarProveedor(id).subscribe(
      (res) => {
        this.getProveedores();
      },
      (err) => console.error('Error al eliminar proveedor:', err)
    );
  }

  // Filtros
  filtrarPorNombre() {
    if (!this.filtroNombre && !this.filtroEstado) {
      this.getProveedores();
      return;
    }
    let filtrados = this.proveedoresOriginal;
    if (this.filtroNombre) {
      filtrados = filtrados.filter((p: any) =>
        p.nombre.toLowerCase().includes(this.filtroNombre.toLowerCase())
      );
    }
    if (this.filtroEstado) {
      filtrados = filtrados.filter((p: any) => p.estado === this.filtroEstado);
    }
    this.proveedores = filtrados;
  }

  filtrarPorEstado() {
    if (!this.filtroNombre && !this.filtroEstado) {
      this.getProveedores();
      return;
    }
    let filtrados = this.proveedoresOriginal;
    if (this.filtroNombre) {
      filtrados = filtrados.filter((p: any) =>
        p.nombre.toLowerCase().includes(this.filtroNombre.toLowerCase())
      );
    }
    if (this.filtroEstado) {
      filtrados = filtrados.filter((p: any) => p.estado === this.filtroEstado);
    }
    this.proveedores = filtrados;
  }

  limpiarFiltros() {
    this.filtroNombre = '';
    this.filtroEstado = '';
    this.getProveedores();
  }
}
