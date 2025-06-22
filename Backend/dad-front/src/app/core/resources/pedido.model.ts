import { Cliente } from './cliente.model';

export type EstadoPedido = 'PENDIENTE' | 'INICIADO' | 'COMPLETADO' | 'CANCELADO';

export interface Pedido {
  id?: number;
  clienteId: number;
  cliente?: Cliente;
  fechaPedido?: string; // formato ISO: '2025-06-22T14:30'
  estadoPedido: EstadoPedido;
  detalles?: any[]; // Puedes definir un modelo DetallePedido luego si lo deseas
}
