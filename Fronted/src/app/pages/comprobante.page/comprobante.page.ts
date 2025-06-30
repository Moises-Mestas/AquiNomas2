import {ApplicationConfig, Component} from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComprobanteService } from '../../core/services/comprobante.services';
import { VentaService } from '../../core/services/venta.services';
import {provideRouter} from '@angular/router';
import {routes} from '../../app.routes';

export const appConfig: ApplicationConfig = {
  providers: [
    provideRouter(routes),
  ]
};

@Component({
  selector: 'app-comprobante',
  standalone: true,
  imports: [CommonModule, FormsModule, DatePipe],
  templateUrl: './comprobante.page.html',
  styleUrls: ['./comprobante.page.css']
})
export class ComprobantePage {
  comprobanteSeleccionado: any = null;
  comprobantes: any[] = [];
  comprobanteForm: any = {
    ventaId: null,
    tipo: 'BOLETA'
  };

  vista: 'listar' | 'crear' = 'listar';
  filtroTipo: string = '';
  filtroFechaInicio: string = '';
  filtroFechaFin: string = '';

  constructor(
    private comprobanteService: ComprobanteService,
    private ventaService: VentaService
  ) {}

  ngOnInit(): void {
    this.obtenerComprobantes();
  }

  obtenerComprobantes(): void {
    this.comprobanteService.listarComprobantes().subscribe(comprobantes => {
      this.comprobantes = comprobantes;

      this.comprobantes.forEach(c => {
        if (!c.venta || !c.venta.pedido?.cliente) {
          this.ventaService.getVentaById(c.ventaId).subscribe(venta => {
            c.venta = venta;
          });
        }
      });
    });
  }

  generar(): void {
    const { ventaId, tipo } = this.comprobanteForm;
    if (ventaId && tipo) {
      this.comprobanteService.generarComprobante(ventaId, tipo).subscribe(() => {
        this.cancelar();
        this.obtenerComprobantes();
      });
    }
  }

  filtrar(): void {
    this.comprobanteService
      .filtrarComprobante(this.filtroTipo, this.filtroFechaInicio, this.filtroFechaFin)
      .subscribe(res => {
        this.comprobantes = res;

        this.comprobantes.forEach(c => {
          this.ventaService.getVentaById(c.ventaId).subscribe(venta => {
            c.venta = venta;
          });
        });
      });
  }

  descargarPDF(id: number): void {
    this.comprobanteService.descargarPDF(id).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `comprobante_${id}.pdf`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  eliminarComprobante(id: number): void {
    if (confirm('¿Deseas eliminar este comprobante?')) {
      this.comprobanteService.eliminarComprobante(id).subscribe(() => this.obtenerComprobantes());
    }
  }

  cancelar(): void {
    this.vista = 'listar';
    this.comprobanteForm = { ventaId: null, tipo: 'BOLETA' };
  }

  verDetalle(comprobante: any): void {
    this.comprobanteSeleccionado = comprobante;
  }

  cerrarDetalle(): void {
    this.comprobanteSeleccionado = null;
  }
}
