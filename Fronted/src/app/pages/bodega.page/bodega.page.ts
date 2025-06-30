import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { BodegaServices } from '../../core/services/bodega.services';

@Component({
  selector: 'app-bodega',
  standalone: true,
  templateUrl: './bodega.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./bodega.page.css'],
})
export class BodegaPage {
  bodegas: any[] = [];
  bodegasOriginal: any[] = [];
  newBodega: any = {
    producto_id: null,
    cantidad: '',
    tipo_insumo: '',
    fecha_entrada: '',
    unidad_medida: '',
    stock_minimo: '',
  };
  idAActualizar: number = 0;
  editing: boolean = false;
  bodegaIdAEliminar: number = 0;

  // Filtros
  filtroTipo: string = '';
  filtroFecha: string = '';
  filtroFechaInicio: string = '';
  filtroFechaFin: string = '';

  constructor(private bodegaService: BodegaServices) {}

  ngOnInit() {
    this.getBodegas();
  }

  getBodegas() {
    this.bodegaService.getBodega().subscribe(
      (response) => {
        this.bodegas = response;
        this.bodegasOriginal = response;
      },
      (err) => console.error('Error al obtener bodegas:', err)
    );
  }

  clearForm() {
    this.newBodega = {
      producto_id: null,
      cantidad: '',
      tipo_insumo: '',
      fecha_entrada: '',
      unidad_medida: '',
      stock_minimo: '',
    };
    this.idAActualizar = 0;
    this.editing = false;
  }

  abrirModalBodega(edit: boolean, bodega: any = null) {
    if (edit && bodega) {
      this.editing = true;
      this.idAActualizar = bodega.id;
      this.newBodega = {
        producto_id: bodega.producto_id,
        cantidad: bodega.cantidad,
        tipo_insumo: bodega.tipo_insumo,
        fecha_entrada: bodega.fecha_entrada,
        unidad_medida: bodega.unidad_medida,
        stock_minimo: bodega.stock_minimo,
      };
    } else {
      this.clearForm();
    }
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalBodega')
      );
      modal.show();
    }, 0);
  }

  abrirModalEliminar(id: number) {
    this.bodegaIdAEliminar = id;
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalEliminarBodega')
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

  saveBodega() {
    if (this.editing) {
      this.bodegaService
        .actualizarBodega(this.idAActualizar, this.newBodega)
        .subscribe(
          (res) => {
            this.getBodegas();
            this.clearForm();
            this.cerrarModal('modalBodega');
          },
          (err) => console.error('Error al actualizar bodega:', err)
        );
    } else {
      this.bodegaService.crearBodega(this.newBodega).subscribe(
        (res) => {
          this.getBodegas();
          this.clearForm();
          this.cerrarModal('modalBodega');
        },
        (err) => console.error('Error al crear bodega:', err)
      );
    }
  }

  confirmarEliminar() {
    this.deleteBodega(this.bodegaIdAEliminar);
    this.cerrarModal('modalEliminarBodega');
  }

  deleteBodega(id: number) {
    this.bodegaService.eliminarBodega(id).subscribe(
      (res) => {
        this.getBodegas();
      },
      (err) => console.error('Error al eliminar bodega:', err)
    );
  }

  // Filtros
  filtrarPorTipo() {
    if (!this.filtroTipo) {
      this.getBodegas();
      return;
    }
    this.bodegaService.getBodegaPorTipo(this.filtroTipo).subscribe(
      (res) => {
        this.bodegas = res;
      },
      (err) => console.error('Error al buscar bodegas por tipo:', err)
    );
  }

  filtrarPorFecha() {
    if (!this.filtroFecha) {
      this.getBodegas();
      return;
    }
    this.bodegaService.getBodegaPorFecha(this.filtroFecha).subscribe(
      (res) => {
        this.bodegas = res;
      },
      (err) => console.error('Error al buscar bodegas por fecha:', err)
    );
  }

  filtrarPorRangoFecha() {
    if (!this.filtroFechaInicio || !this.filtroFechaFin) {
      this.getBodegas();
      return;
    }
    this.bodegaService
      .getBodegaPorRangoFecha(this.filtroFechaInicio, this.filtroFechaFin)
      .subscribe(
        (res) => {
          this.bodegas = res;
        },
        (err) =>
          console.error('Error al buscar bodegas por rango de fecha:', err)
      );
  }

  limpiarFiltros() {
    this.filtroTipo = '';
    this.filtroFecha = '';
    this.filtroFechaInicio = '';
    this.filtroFechaFin = '';
    this.getBodegas();
  }
}
