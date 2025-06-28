import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComprasProveedoresServices } from '../../core/services/compras-proveedores.services';
import { ProductoServices } from '../../core/services/producto.services';
import { ProveedorServices } from '../../core/services/proveedores.services';

@Component({
  selector: 'app-compras-proveedores',
  standalone: true,
  templateUrl: './compras-proveedores.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./compras-proveedores.page.css'],
})
export class ComprasProveedoresPage {
  compras: any[] = [];
  comprasOriginal: any[] = [];
  newCompra: any = {
    proveedor_id: '',
    producto_id: '',
    cantidad: '',
    unidad_medida: '',
  };

  // Filtros
  filtroFecha: string = '';
  filtroFechaInicio: string = '';
  filtroFechaFin: string = '';

  // Para selects
  proveedores: any[] = [];
  productos: any[] = [];

  constructor(
    private comprasService: ComprasProveedoresServices,
    private productoService: ProductoServices,
    private proveedorService: ProveedorServices
  ) {}

  ngOnInit() {
    this.getCompras();
    this.getProveedores();
    this.getProductos();
  }

  getCompras() {
    this.comprasService.getCompras().subscribe(
      (response) => {
        this.compras = response;
        this.comprasOriginal = response;
      },
      (err) => console.error('Error al obtener compras:', err)
    );
  }

  getProveedores() {
    this.proveedorService.getProveedores().subscribe(
      (res) => (this.proveedores = res),
      (err) => console.error('Error al obtener proveedores:', err)
    );
  }

  getProductos() {
    this.productoService.getProductos().subscribe(
      (res) => (this.productos = res),
      (err) => console.error('Error al obtener productos:', err)
    );
  }

  clearForm() {
    this.newCompra = {
      proveedor_id: '',
      producto_id: '',
      cantidad: '',
      unidad_medida: '',
    };
  }

  abrirModalCompra() {
    this.clearForm();
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalCompra')
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

  saveCompra() {
    // Forzamos a número los IDs antes de enviar
    this.newCompra.proveedor_id = Number(this.newCompra.proveedor_id);
    this.newCompra.producto_id = Number(this.newCompra.producto_id);

    this.comprasService.crearCompra(this.newCompra).subscribe(
      (res) => {
        this.getCompras();
        this.clearForm();
        this.cerrarModal('modalCompra');
      },
      (err) => console.error('Error al crear compra:', err)
    );
  }

  // Filtros
  filtrarPorFecha() {
    if (!this.filtroFecha) {
      this.getCompras();
      return;
    }
    this.comprasService.getComprasPorFecha(this.filtroFecha).subscribe(
      (res) => {
        this.compras = res;
      },
      (err) => console.error('Error al buscar compras por fecha:', err)
    );
  }

  filtrarPorRangoFecha() {
    if (!this.filtroFechaInicio || !this.filtroFechaFin) {
      this.getCompras();
      return;
    }
    this.comprasService
      .getComprasPorRangoFecha(this.filtroFechaInicio, this.filtroFechaFin)
      .subscribe(
        (res) => {
          this.compras = res;
        },
        (err) =>
          console.error('Error al buscar compras por rango de fecha:', err)
      );
  }

  limpiarFiltros() {
    this.filtroFecha = '';
    this.filtroFechaInicio = '';
    this.filtroFechaFin = '';
    this.getCompras();
  }
}
