import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ReporteService } from '../../core/services/reporte.service';

@Component({
  selector: 'app-reportes',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reportes.component.html',
  styleUrl: './reportes.component.css'
})

export class ReportesComponent implements OnInit {
  productosRentables: any[] = [];

  constructor(private reporteService: ReporteService) {}

  ngOnInit(): void {
    this.reporteService.getProductosMasRentables().subscribe({
      next: (data) => this.productosRentables = data,
      error: (err) => console.error('Error al cargar reportes', err)
    });
  }
}
