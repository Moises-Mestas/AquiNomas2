import { Menu } from './menu.model';
import { Producto } from './producto.model';

export interface Receta {
  id?: number;
  productoId: number;
  producto?: Producto;
  descripcion: string;
  cantidad: number;
  unidadMedida?: string;
  menu: Menu;
  cantidadDisponible: number;
}
