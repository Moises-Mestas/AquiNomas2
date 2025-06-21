import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RecetasService, Receta } from '../../core/services/recetas.service';

@Component({
  selector: 'app-recetas',
  standalone: true,
  templateUrl: './recetas.html',
  styleUrls: ['./recetas.scss'],
  imports: [CommonModule, FormsModule]
})
export class Recetas implements OnInit {
  recetas: Receta[] = [];

  constructor(private recetasService: RecetasService) {}

  ngOnInit(): void {
    this.cargarRecetas();
  }

  cargarRecetas(): void {
    this.recetasService.getAll().subscribe({
      next: (data) => this.recetas = data,
      error: (err) => console.error('Error al cargar recetas:', err)
    });
  }
}
