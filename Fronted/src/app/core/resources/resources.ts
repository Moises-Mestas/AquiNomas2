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
  proveedorPorId: (id: number) => `proveedores/${id}`,
  proveedorPorNombre: (nombre: string) => `proveedores/nombre/${nombre}`,
  proveedorPorEstado: (estado: string) => `proveedores/estado/${estado}`,

  comprasProveedores: 'compras-proveedores',
  compraProveedorPorId: (id: number) => `compras-proveedores/${id}`,
  comprasPorFecha: 'compras/fecha',
  comprasPorFechaEspecifica: 'compras/fecha-especifica',

  bodega: 'bodega',
  inventarioBarra: 'inventario-barra',
  inventarioCocina: 'inventario-cocina',
};
