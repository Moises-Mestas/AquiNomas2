export const resources = {
  menus: 'menus',
  recetas: 'recetas',
  pedidos: 'pedidos',
  detalle_pedidos: 'detalle-pedidos',

  productos: 'productos',
  productoPorId: (id: number) => `productos/${id}`,
  productoPorNombre: (nombre: string) => `productos/nombre/${nombre}`,
  productoPorTipo: (tipo: string) => `productos/tipo/${tipo}`,
  productoPorPrecio: 'productos/precio',

  proveedores: 'proveedores',
  bodega: 'bodega',
  inventarioBarra: 'inventario-barra',
  inventarioCocina: 'inventario-cocina',

  comprasProveedores: 'compras-proveedores',
};
