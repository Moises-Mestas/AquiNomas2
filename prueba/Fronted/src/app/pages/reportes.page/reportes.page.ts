import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReporteService } from '../../core/services/reporte.services';
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './reportes.page.html',
  styleUrls: ['./reportes.page.css']
})
export class ReportesPage {
  productosMasRentables: any[] = [];
  platosBebidasMas: any[] = [];
  platosBebidasMenos: any[] = [];
  reportes: any[] = [];

  constructor(private reporteService: ReporteService) {}

  ngOnInit() {
    this.getProductosMasRentables();
    this.getPlatosBebidas();
    this.getTodosReportes();
  }

  getProductosMasRentables() {
    this.reporteService.getProductosMasRentables().subscribe(res => {
      this.productosMasRentables = res;
    });
  }

  exportarPdfProductosMasRentables() {
    this.reporteService.exportarProductosMasRentables().subscribe(blob => {
      this.descargarBlob(blob, 'productos-mas-rentables.pdf');
    });
  }

  getPlatosBebidas() {
    this.reporteService.getPlatosBebidas().subscribe(res => {
      this.platosBebidasMas = res.masPedidos || [];
      this.platosBebidasMenos = res.menosPedidos || [];
    });
  }

  exportarPdfPlatosBebidas() {
    this.reporteService.exportarPlatosBebidas().subscribe(blob => {
      this.descargarBlob(blob, 'platos-bebidas.pdf');
    });
  }

  getTodosReportes() {
    this.reporteService.getTodosReportes().subscribe(res => {
      this.reportes = res;
    });
  }

  exportarPdfTodosReportes() {
    this.reporteService.exportarTodosReportes().subscribe(blob => {
      this.descargarBlob(blob, 'reporte-general.pdf');
    });
  }

  private descargarBlob(blob: Blob, nombreArchivo: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = nombreArchivo;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
