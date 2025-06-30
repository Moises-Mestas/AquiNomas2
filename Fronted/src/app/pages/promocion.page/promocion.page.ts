import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PromocionService } from '../../core/services/promocion.services';
import { MenuService } from '../../core/services/menu.services';

@Component({
  selector: 'app-promocion',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './promocion.page.html',
  styleUrls: ['./promocion.page.css']
})
export class PromocionPage {
  promociones: any[] = [];
  listaMenus: any[] = [];
  promocionSeleccionada: any = null;

  promocionForm: any = {
    nombre: '',
    tipoDescuento: '',
    valorDescuento: 0,
    montoMinimo: null,
    cantidadMinima: null,
    fechaInicio: '',
    fechaFin: '',
    menuSeleccionado: []
  };

  idEditando: number | null = null;
  vista: 'listar' | 'crear' | 'editar' = 'listar';
  filtroNombre = '';

  constructor(
    private promocionService: PromocionService,
    private menuService: MenuService
  ) {}

  ngOnInit(): void {
    this.menuService.getMenus().subscribe(res => {
      this.listaMenus = res.map(menu => ({ ...menu, cantidad: 0 }));
    });
    this.listarPromociones();
  }

  listarPromociones(): void {
    this.promocionService.listarTodasPromociones().subscribe(res => this.promociones = res);
  }

  buscarPorNombre(): void {
    const nombre = this.filtroNombre.trim();
    if (nombre) {
      this.promocionService.buscarPorNombrePromocion(nombre).subscribe(res => {
        this.promociones = res;
      });
    } else {
      this.listarPromociones();
    }
  }

  obtenerCantidad(menuId: number): number {
    const encontrado = this.promocionForm.menuSeleccionado.find((m: any) => m.menuId === menuId);
    return encontrado?.cantidadRequerida || 1;
  }

  toggleMenuSeleccionado(menu: any): void {
    const index = this.promocionForm.menuSeleccionado.findIndex((m: any) => m.menuId === menu.id);
    if (index !== -1) {
      this.promocionForm.menuSeleccionado.splice(index, 1);
    } else {
      this.promocionForm.menuSeleccionado.push({ menuId: menu.id, cantidadRequerida: menu.cantidad || 1 });
    }
  }

  yaSeleccionado(menuId: number): boolean {
    return this.promocionForm.menuSeleccionado.some((m: any) => m.menuId === menuId);
  }

  actualizarCantidad(menu: any): void {
    const i = this.promocionForm.menuSeleccionado.findIndex((m: any) => m.menuId === menu.id);
    if (i !== -1) {
      this.promocionForm.menuSeleccionado[i].cantidadRequerida = menu.cantidad || 1;
    }
  }


  guardar(): void {
    const payload = {
      ...this.promocionForm,
      menu: this.promocionForm.menuSeleccionado // 👈 se alinea con backend
    };

    if (this.vista === 'editar' && this.idEditando) {
      this.promocionService.actualizarPromocion(this.idEditando, payload).subscribe(() => this.reiniciar());
    } else {
      this.promocionService.crearPromocion(payload).subscribe(() => this.reiniciar());
    }
  }

  editar(promo: any): void {
    this.promocionForm = {
      nombre: promo.nombre,
      tipoDescuento: promo.tipoDescuento,
      valorDescuento: promo.valorDescuento,
      montoMinimo: promo.montoMinimo,
      cantidadMinima: promo.cantidadMinima,
      fechaInicio: promo.fechaInicio,
      fechaFin: promo.fechaFin,
      menuSeleccionado: promo.menu || []
    };
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
      tipoDescuento: '',
      valorDescuento: 0,
      montoMinimo: null,
      cantidadMinima: null,
      fechaInicio: '',
      fechaFin: '',
      menuSeleccionado: []
    };
    this.idEditando = null;
    this.vista = 'listar';
    this.filtroNombre = '';
    this.listarPromociones();
  }

  abrirVistaDetalle(promo: any): void {
    this.promocionSeleccionada = promo;
  }

  cerrarVistaDetalle(): void {
    this.promocionSeleccionada = null;
  }
}
