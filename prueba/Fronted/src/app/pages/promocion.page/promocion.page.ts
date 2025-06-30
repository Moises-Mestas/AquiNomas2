import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PromocionService } from '../../core/services/promocion.services';

@Component({
  selector: 'app-promocion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './promocion.page.html',
  styleUrls: ['./promocion.page.css']
})
export class PromocionPage {
  promociones: any[] = [];
  promocionForm: any = {
    nombre: '',
    tipo: '',
    descuento: 0,
    fechaInicio: '',
    fechaFin: ''
  };
  idEditando: number | null = null;
  vista: 'listar' | 'crear' | 'editar' = 'listar';
  filtroNombre = '';

  constructor(private promocionService: PromocionService) {}

  ngOnInit(): void {
    this.listarPromociones();
  }

  listarPromociones(): void {
    this.promocionService.listarTodasPromociones().subscribe(res => this.promociones = res);
  }

  buscarPorNombre(): void {
    if (this.filtroNombre.trim()) {
      this.promocionService.buscarPorNombrePromocion(this.filtroNombre.trim()).subscribe(res => {
        this.promociones = res;
      });
    } else {
      this.listarPromociones();
    }
  }

  guardar(): void {
    if (this.vista === 'editar' && this.idEditando) {
      this.promocionService.actualizarPromocion(this.idEditando, this.promocionForm).subscribe(() => this.reiniciar());
    } else {
      this.promocionService.crearPromocion(this.promocionForm).subscribe(() => this.reiniciar());
    }
  }

  editar(promo: any): void {
    this.promocionForm = { ...promo };
    this.idEditando = promo.id;
    this.vista = 'editar';
  }

  eliminar(id: number): void {
    if (confirm('¿Eliminar esta promoción?')) {
      this.promocionService.eliminarPromocion(id).subscribe(() => this.listarPromociones());
    }
  }

  cancelar(): void {
    this.reiniciar();
  }

  reiniciar(): void {
    this.promocionForm = {
      nombre: '',
      tipo: '',
      descuento: 0,
      fechaInicio: '',
      fechaFin: ''
    };
    this.idEditando = null;
    this.vista = 'listar';
    this.filtroNombre = '';
    this.listarPromociones();
  }
}
