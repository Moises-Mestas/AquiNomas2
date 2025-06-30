import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { InventarioCocinaServices } from '../../core/services/inventario-cocina.services';
import { AuthService } from '../../core/services/auth.services';

@Component({
  selector: 'app-inventario-cocina',
  standalone: true,
  templateUrl: './inventario-cocina.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./inventario-cocina.page.css'],
})
export class InventarioCocinaPage {
  inventarioCocina: any[] = [];
  inventarioCocinaOriginal: any[] = [];
  newInventario: any = {
    bodega_id: null,
    cantidad_disponible: '',
    stock_minimo: '',
    unidad_destino: 'kg',
  };
  idAEliminar: number = 0;

  // Filtros
  mostrarAlertaStock: boolean = false;
  alertaStock: any[] = [];

  constructor(
    private inventarioCocinaService: InventarioCocinaServices,
    private authService: AuthService
  ) {}

  logout() {
    this.authService.logout();
  }

  ngOnInit() {
    this.getInventarioCocina();
  }

  getInventarioCocina() {
    this.inventarioCocinaService.getInventarioCocina().subscribe(
      (response) => {
        this.inventarioCocina = response;
        this.inventarioCocinaOriginal = response;
      },
      (err) => console.error('Error al obtener inventario cocina:', err)
    );
  }

  clearForm() {
    this.newInventario = {
      bodega_id: null,
      cantidad_disponible: '',
      stock_minimo: '',
      unidad_destino: 'kg',
    };
  }

  abrirModalInventario() {
    this.clearForm();
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalInventarioCocina')
      );
      modal.show();
    }, 0);
  }

  abrirModalEliminar(id: number) {
    this.idAEliminar = id;
    setTimeout(() => {
      const modal = new (window as any).bootstrap.Modal(
        document.getElementById('modalEliminarInventarioCocina')
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
    this.inventarioCocinaService
      .crearInventarioCocinaDesdeBodega(this.newInventario)
      .subscribe(
        (res) => {
          this.getInventarioCocina();
          this.clearForm();
          this.cerrarModal('modalInventarioCocina');
        },
        (err) => console.error('Error al crear inventario cocina:', err)
      );
  }

  confirmarEliminar() {
    this.deleteInventario(this.idAEliminar);
    this.cerrarModal('modalEliminarInventarioCocina');
  }

  deleteInventario(id: number) {
    this.inventarioCocinaService.eliminarInventarioCocina(id).subscribe(
      (res) => {
        this.getInventarioCocina();
      },
      (err) => console.error('Error al eliminar inventario cocina:', err)
    );
  }

  mostrarAlertaStockMinimo() {
    this.inventarioCocinaService.getAlertaStockMinimo().subscribe(
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
