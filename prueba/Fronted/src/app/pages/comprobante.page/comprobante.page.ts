import { Component } from '@angular/core';
import { CommonModule, CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ComprobanteService } from '../../core/services/comprobante.services';

@Component({
  selector: 'app-comprobante',
  standalone: true,
  imports: [CommonModule, FormsModule, CurrencyPipe, DatePipe],
  templateUrl: './comprobante.page.html',
  styleUrls: ['./comprobante.page.css']
})
export class ComprobantePage {
  comprobantes: any[] = [];
  comprobanteForm: any = {
    ventaId: null,
    tipo: 'BOLETA'
  };

  vista: 'listar' | 'crear' = 'listar';
  filtroTipo: string = '';
  filtroFechaInicio: string = '';
  filtroFechaFin: string = '';

  constructor(private comprobanteService: ComprobanteService) {}

  ngOnInit() {
    this.obtenerComprobantes();
  }

  obtenerComprobantes(): void {
    this.comprobanteService.listarComprobantes().subscribe(res => {
      this.comprobantes = res;
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
      .subscribe(res => (this.comprobantes = res));
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
}
