import { Menu } from './menu.model';
import { Pedido } from './pedido.model';

export interface DetallePedido {
  id?: number;
  pedido: Pedido;   // Se puede usar solo el id si prefieres
  menu: Menu;
  cantidad: number;
}
