import { Component } from '@angular/core';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { VentaService } from '../../core/services/venta.services';

@Component({
  selector: 'app-venta',
  standalone: true,
  templateUrl: './venta.page.html',
  imports: [CommonModule, FormsModule, CurrencyPipe],
  styleUrls: ['./venta.page.css']
})
export class VentaPage {
  ventas: any[] = [];
  pedidosNoVendidos: any[] = [];
  promocionesActivas: any[] = [];

  ventaForm: any = {
    cliente: '',
    pedidoId: null,
    metodoPago: 'EFECTIVO',
    total: null
  };

  vista: 'listar' | 'crear' | 'editar' = 'listar';
  ventaIdEditando: number | null = null;

  // Filtros
  filtroNombreCliente = '';
  filtroMetodoPago = '';
  filtroFechaInicio = '';
  filtroFechaFin = '';

  constructor(private ventaService: VentaService) {}

  ngOnInit() {
    this.obtenerVentas();
  }

  obtenerVentas(): void {
    this.ventaService.getVentas().subscribe(res => this.ventas = res);
  }

  guardarVenta(): void {
    if (this.vista === 'editar' && this.ventaIdEditando) {
      this.ventaService.actualizarVenta(this.ventaIdEditando, this.ventaForm).subscribe(() => {
        this.cancelar();
        this.obtenerVentas();
      });
    } else {
      this.ventaService.crearVenta(this.ventaForm).subscribe(() => {
        this.cancelar();
        this.obtenerVentas();
      });
    }
  }

  editarVenta(venta: any): void {
    this.ventaForm = { ...venta };
    this.ventaIdEditando = venta.id;
    this.vista = 'editar';
  }

  eliminarVenta(id: number): void {
    if (confirm('¿Seguro que deseas eliminar esta venta?')) {
      this.ventaService.eliminarVenta(id).subscribe(() => this.obtenerVentas());
    }
  }

  cancelar(): void {
    this.ventaForm = { cliente: '', pedidoId: null, metodoPago: 'EFECTIVO', total: null };
    this.ventaIdEditando = null;
    this.vista = 'listar';
    this.promocionesActivas = [];
    this.pedidosNoVendidos = [];
  }

  aplicarFiltro(): void {
    if (this.filtroFechaInicio && this.filtroFechaFin) {
      this.ventaService.getVentasPorFecha(this.filtroFechaInicio, this.filtroFechaFin).subscribe(res => this.ventas = res);
    } else if (this.filtroMetodoPago) {
      this.ventaService.getVentasPorMetodoPago(this.filtroMetodoPago).subscribe(res => this.ventas = res);
    } else if (this.filtroNombreCliente) {
      this.ventaService.getPedidosPorNombreCliente(this.filtroNombreCliente).subscribe(res => this.ventas = res); // ⚠️ ajustar si esta API devuelve ventas
    } else {
      this.obtenerVentas();
    }
  }

  verPromociones(pedidoId: number): void {
    this.ventaService.getPromocionesAplicables(pedidoId).subscribe(res => {
      this.promocionesActivas = res.promociones || res;
    });
  }

  cargarPedidosNoVendidos(clienteId: number): void {
    this.ventaService.getPedidosNoVendidosPorCliente(clienteId).subscribe(res => {
      this.pedidosNoVendidos = res;
    });
  }
}
