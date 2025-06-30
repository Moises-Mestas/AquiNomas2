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
  ventasOriginales: any[] = [];
  ventas: any[] = [];
  pedidosEncontrados: any[] = [];
  pedidosNoVendidos: any[] = [];
  promocionesActivas: any[] = [];

  ventaForm: any = {
    cliente: '',
    pedidoId: null,
    metodoPago: 'EFECTIVO',
    total: null,
    promociones: [],
    maximoPromocionesPermitidas: 1
  };

  vista: 'listar' | 'crear' | 'editar' | 'buscarPedidos' | 'promociones' = 'listar';
  ventaIdEditando: number | null = null;

  filtroNombreCliente = '';
  filtroMetodoPago = '';
  filtroFechaInicio = '';
  filtroFechaFin = '';

  nombreBusqueda = '';
  pedidoSeleccionado: number | null = null;
  pedidoAnalizado: any = null;

  constructor(private ventaService: VentaService) {}

  ngOnInit(): void {
    this.obtenerVentas();
  }

  obtenerVentas(): void {
    this.ventaService.getVentas().subscribe(res => {
      this.ventasOriginales = res;
      this.ventas = res;
    });
  }

  guardarVenta(): void {
    const dataAEnviar = {
      metodoPago: this.ventaForm.metodoPago,
      promociones: this.ventaForm.promociones,
      maximoPromocionesPermitidas: this.ventaForm.maximoPromocionesPermitidas
    };

    if (this.vista === 'editar' && this.ventaIdEditando) {
      this.ventaService.actualizarVenta(this.ventaIdEditando, dataAEnviar).subscribe(() => {
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

  editarVenta(v: any): void {
    this.vista = 'editar';
    this.ventaIdEditando = v.id;
    this.ventaForm = {
      pedidoId: v.pedidoId,
      metodoPago: v.metodoPago,
      promociones: v.promociones || [],
      maximoPromocionesPermitidas: v.maximoPromocionesPermitidas || 1,
      total: v.total
    };
  }

  eliminarVenta(id: number): void {
    if (confirm('¿Seguro que deseas eliminar esta venta?')) {
      this.ventaService.eliminarVenta(id).subscribe(() => this.obtenerVentas());
    }
  }

  cancelar(): void {
    this.ventaForm = {
      cliente: '',
      pedidoId: null,
      metodoPago: 'EFECTIVO',
      total: null,
      promociones: [],
      maximoPromocionesPermitidas: 1
    };
    this.ventaIdEditando = null;
    this.vista = 'listar';
    this.promocionesActivas = [];
    this.pedidosNoVendidos = [];
    this.pedidoSeleccionado = null;
    this.pedidoAnalizado = null;
    this.nombreBusqueda = '';
    this.pedidosEncontrados = [];
  }

  aplicarFiltro(): void {
    if (this.filtroFechaInicio && this.filtroFechaFin) {
      this.ventaService.getVentasPorFecha(this.filtroFechaInicio, this.filtroFechaFin).subscribe(res => this.ventas = res);
    } else if (this.filtroMetodoPago) {
      this.ventaService.getVentasPorMetodoPago(this.filtroMetodoPago).subscribe(res => this.ventas = res);
    } else if (this.filtroNombreCliente) {
      this.buscarPedidosPorCliente();
    } else {
      this.obtenerVentas();
    }
  }

  buscarPedidosPorCliente(): void {
    if (this.nombreBusqueda.trim()) {
      this.ventaService.getPedidosPorNombreCliente(this.nombreBusqueda.trim()).subscribe(res => this.pedidosEncontrados = res);
    }
  }

  analizarPromociones(): void {
    if (this.pedidoSeleccionado) {
      this.ventaService.getPromocionesAplicables(this.pedidoSeleccionado).subscribe(
        res => {
          this.promocionesActivas = res.promocionesAplicables || [];
          this.pedidoAnalizado = this.pedidoSeleccionado;
        },
        err => {
          console.error('Error al obtener promociones:', err);
          this.promocionesActivas = [];
        }
      );
    }
  }

  cargarPedidosNoVendidos(cliente: string): void {
    this.ventaService.getPedidosNoVendidosPorCliente(cliente).subscribe(res => this.pedidosNoVendidos = res);
  }

  transformarPromociones(entrada: string): void {
    if (!entrada) {
      this.ventaForm.promociones = [];
      return;
    }
    this.ventaForm.promociones = entrada
      .split(',')
      .map(id => id.trim())
      .filter(id => !!id && !isNaN(+id))
      .map(id => ({ id: +id }));
  }

  filtrarVentasCombinado(): void {
    this.ventas = this.ventasOriginales.filter(venta => {
      const nombreCoincide = this.filtroNombreCliente
        ? venta.pedido?.cliente?.nombre?.toLowerCase().includes(this.filtroNombreCliente.toLowerCase())
        : true;

      const metodoCoincide = this.filtroMetodoPago
        ? venta.metodoPago === this.filtroMetodoPago
        : true;

      const fechaCoincide = (this.filtroFechaInicio && this.filtroFechaFin)
        ? new Date(venta.fechaVenta) >= new Date(this.filtroFechaInicio) &&
        new Date(venta.fechaVenta) <= new Date(this.filtroFechaFin)
        : true;

      return nombreCoincide && metodoCoincide && fechaCoincide;
    });
  }

}
