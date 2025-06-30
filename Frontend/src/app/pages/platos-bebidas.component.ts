import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // 👈 necesario para *ngFor y *ngIf
import { HttpClientModule } from '@angular/common/http';
import { ReporteService } from '../core/services/reporte.service';

@Component({
  selector: 'app-platos-bebidas',
  standalone: true,
  templateUrl: './platos-bebidas.component.html',
  styleUrls: ['./platos-bebidas.component.css'],
  imports: [CommonModule, HttpClientModule], // ✅ aquí está la solución
})
export class PlatosBebidasComponent implements OnInit {
  masPedidos: any[] = [];
  menosPedidos: any[] = [];

  constructor(private reporteService: ReporteService) {}

  ngOnInit(): void {
    this.reporteService.getPlatosBebidas().subscribe({
      next: (data) => {
        this.masPedidos = data.masPedidos ?? [];
        this.menosPedidos = data.menosPedidos ?? [];
      },
      error: (err) => {
        console.error('Error al cargar platos y bebidas:', err);
      }
    });
  }
}
