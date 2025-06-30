export const resources = {
  // CLIENTES
  clientes: 'clientes',
  clientePorId: (id: number) => `clientes/${id}`,
  clientePorDni: (dni: string) => `clientes/dni/${dni}`,
  clientePorNombre: (nombre: string) => `clientes/nombre/${nombre}`,

  // ADMINISTRADORES (si usas en frontend)
  administradores: 'administradores',
  adminPorId: (id: number) => `administradores/${id}`,
  adminPorNombre: (nombre: string) => `administradores/nombre/${nombre}`,

  // OTROS (si más adelante agregas)
  // ejemplo: 'clientes/activo', 'clientes/ordenados', etc.
};
