import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {ReporteService} from '../../core/services/reporte.services';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reportes.page.html',
  styleUrls: ['./reportes.page.css']
})
export class ReportesPage implements OnInit {
  productosRentables: any[] = [];

  constructor(private reporteService: ReporteService) {}

  ngOnInit(): void {
    this.reporteService.getProductosMasRentables().subscribe({
      next: (data) => {
        this.productosRentables = data;
        console.log('Productos rentables:', data);
      },
      error: (err) => {
        console.error('Error al obtener productos rentables', err);
      }
    });
  }
}
