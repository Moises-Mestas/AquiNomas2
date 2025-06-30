export const resources = {
  menus: 'menus',
  recetas: 'recetas',
  pedidos: 'pedidos',
  detalle_pedidos: 'detalle-pedidos',
  clientes: 'clientes',

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
  bodegaPorId: (id: number) => `bodega/${id}`,
  bodegaPorTipo: (tipo: string) => `bodega/tipo-insumo/${tipo}`,
  bodegaPorFecha: 'bodega/fecha-entrada',
  bodegaPorRangoFecha: 'bodega/rango-fecha-entrada',
  bodegaPorProducto: (id: number) => `bodega/producto/${id}`,
  bodegaStockTotal: (id: number) => `bodega/stock-total/${id}`,
  bodegaStockBajo: 'bodega/stock-bajo',
  bodegaHistorial: (id: number) => `bodega/historial/${id}`,

  inventarioBarra: 'inventario-barra',
  inventarioBarraPorId: (id: number) => `inventario-barra/${id}`,
  inventarioBarraDesdeBodega: 'inventario-barra/desde-bodega',
  inventarioBarraAlertaStockMinimo: 'inventario-barra/alerta-stock-minimo',

  inventarioCocina: 'inventario-cocina',
  inventarioCocinaPorId: (id: number) => `inventario-cocina/${id}`,
  inventarioCocinaCrear: 'inventario-cocina/crear',
  inventarioCocinaAlertaStockMinimo: 'inventario-cocina/alerta-stock-minimo',

  auth: 'auth',
  authLogin: 'auth/login',
  authRegister: 'auth/register',
  authLogout: 'auth/logout',

  ventas: 'ventas',
  promociones: 'promociones',
  comprobantes: 'comprobantes',


};
