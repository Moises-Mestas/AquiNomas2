export interface Cliente {
  id?: number;
  nombre: string;
  apellido: string;
  dni?: string;
  ruc?: string;
  telefono?: string;
  email?: string;
  direccion?: string;
  fechaRegistro?: string; // O Date, según tu backend
}
