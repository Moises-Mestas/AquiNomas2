import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InventarioBarraServices } from '../../core/services/inventario-barra.services';

@Component({
  selector: 'app-inventario-barra',
  standalone: true,
  templateUrl: './inventario-barra.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./inventario-barra.page.css'],
})
export class InventarioBarraPage {
  inventarioBarra: any[] = [];
  inventarioBarraOriginal: any[] = [];
  newInventario: any = {
    bodega_id: null,
    cantidad_disponible: '',
    stock_minimo: '',
    unidad_destino: 'l',
  };
  idAEliminar: number = 0;

  // Filtros
  mostrarAlertaStock: boolean = false;
  alertaStock: any[] = [];

  constructor(private inventarioBarraService: InventarioBarraServices) {}

  ngOnInit() {
    this.getInventarioBarra();
  }

  getInventarioBarra() {
    this.inventarioBarraService.getInventarioBarra().subscribe(
      (response) => {
        this.inventarioBarra = response;
        this.inventarioBarraOriginal = response;
      },
      (err) => console.error('Error al obtener inventario barra:', err)
    );
  }

  clearForm() {
    this.newInventario = {
      bodega_id: null,
      cantidad_disponible: '',
      stock_minimo: '',
      unidad_destino: 'l',
    };
  }

  abrirModalInventario() {
    this.clearForm();
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalInventarioBarra')
      );
      modal.show();
    }, 0);
  }

  abrirModalEliminar(id: number) {
    this.idAEliminar = id;
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalEliminarInventarioBarra')
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

  saveInventario() {
    this.inventarioBarraService
      .crearInventarioBarraDesdeBodega(this.newInventario)
      .subscribe(
        (res) => {
          this.getInventarioBarra();
          this.clearForm();
          this.cerrarModal('modalInventarioBarra');
        },
        (err) => console.error('Error al crear inventario barra:', err)
      );
  }

  confirmarEliminar() {
    this.deleteInventario(this.idAEliminar);
    this.cerrarModal('modalEliminarInventarioBarra');
  }

  deleteInventario(id: number) {
    this.inventarioBarraService.eliminarInventarioBarra(id).subscribe(
      (res) => {
        this.getInventarioBarra();
      },
      (err) => console.error('Error al eliminar inventario barra:', err)
    );
  }

  mostrarAlertaStockMinimo() {
    this.inventarioBarraService.getAlertaStockMinimo().subscribe(
      (res) => {
        this.alertaStock = res;
        this.mostrarAlertaStock = true;
      },
      (err) => console.error('Error al obtener alerta stock mínimo:', err)
    );
  }

  ocultarAlertaStockMinimo() {
    this.mostrarAlertaStock = false;
    this.alertaStock = [];
  }
}
