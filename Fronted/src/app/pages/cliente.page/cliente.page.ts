import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ClienteService } from '../../core/services/cliente.services';
import { AuthService } from '../../core/services/auth.services';

@Component({
  selector: 'app-cliente',
  standalone: true,
  templateUrl: './cliente.page.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./cliente.page.css'],
})
export class ClientePage {
  clientes: any[] = []; // Lista de clientes
  newCliente: any = {
    id: 0,
    nombre: '',
    apellido: '',
    dni: '',
    telefono: '',
    email: '',
    direccion: '',
    ruc: '',
    fechaRegistro: '',
  }; // Cliente nuevo
  idAActualizar: number = 0; // ID del cliente a actualizar
  editing: boolean = false; // Control de edición

  // Paginación
  displayedClientes: any[] = []; // Clientes mostrados en la página actual
  currentPage: number = 1;
  clientesPerPage: number = 10; // Limitar a 10 clientes por página
  totalPages: number = 1;

  constructor(
    private clienteService: ClienteService,
    private authService: AuthService
  ) {}

  logout() {
    this.authService.logout();
  }

  ngOnInit() {
    this.getClientes(); // Llama al método para cargar los clientes
  }

  // Obtener todos los clientes
  getClientes() {
    this.clienteService.getClientes().subscribe(
      (response) => {
        // Ordenar los clientes por ID de más reciente a más antiguo (descendente)
        this.clientes = response.sort((a: any, b: any) => b.id - a.id);

        // Calcular el número total de páginas
        this.totalPages = Math.ceil(
          this.clientes.length / this.clientesPerPage
        );

        // Cargar los clientes de la página actual
        this.loadPage(this.currentPage);
      },
      (err) => console.error('Error al obtener clientes:', err)
    );
  }

  // Cambiar la página actual
  changePage(page: number) {
    if (page < 1 || page > this.totalPages) {
      return; // No cambiar si la página es inválida
    }
    this.currentPage = page;
    this.loadPage(page);
  }

  // Cargar los clientes de la página seleccionada
  loadPage(page: number) {
    const startIndex = (page - 1) * this.clientesPerPage;
    const endIndex = page * this.clientesPerPage;
    this.displayedClientes = this.clientes.slice(startIndex, endIndex); // Mostrar solo los clientes de la página actual
  }

  // Guardar cliente: Detecta si es nuevo o si es una actualización
  saveCliente() {
    console.log('Datos a enviar:', this.newCliente);

    if (this.idAActualizar) {
      // Si existe un ID, estamos editando el cliente
      this.updateCliente();
    } else {
      // Si no existe ID, estamos creando un nuevo cliente
      this.createCliente();
    }
  }

  // Crear un nuevo cliente
  createCliente() {
    console.log('Datos a crear:', this.newCliente); // Verificar datos

    // Verificar que los campos obligatorios estén presentes
    if (!this.newCliente.nombre || !this.newCliente.apellido) {
      console.error('Datos faltantes:', this.newCliente);
      alert('El nombre y apellido son campos obligatorios.');
      return;
    }

    // Enviar los datos del cliente al servicio para crear en el backend
    this.clienteService.createCliente(this.newCliente).subscribe(
      (res) => {
        console.log('Cliente creado:', res);
        alert('¡Creación exitosa del cliente!'); // Cambio aquí: mensaje de éxito
        this.getClientes(); // Refresca la lista de clientes
        this.clearForm(); // Limpia el formulario
      },
      (err) => {
        console.error('Error al crear el cliente:', err);
        alert('Hubo un error al crear el cliente');
      }
    );
  }

  // Actualizar un cliente
  updateCliente() {
    console.log('Datos a actualizar:', this.newCliente); // Verificar datos de nuevo cliente
    console.log('ID a actualizar:', this.idAActualizar); // Verificar el ID

    if (!this.idAActualizar) {
      console.error('No se especificó el ID del cliente');
      alert('No se especificó el ID del cliente');
      return;
    }

    // Verificar que los campos obligatorios estén presentes
    if (!this.newCliente.nombre || !this.newCliente.apellido) {
      console.error('Datos faltantes:', this.newCliente);
      alert('El nombre y apellido son campos obligatorios.');
      return;
    }

    // Enviar el ID en el cuerpo de la solicitud
    this.newCliente.id = this.idAActualizar;

    // Actualizar el cliente
    this.clienteService
      .updateCliente(this.idAActualizar, this.newCliente)
      .subscribe(
        (res) => {
          console.log('Cliente actualizado:', res);
          alert('¡Actualización exitosa del cliente!');
          this.getClientes(); // Refrescar la lista de clientes
          this.clearForm(); // Limpiar el formulario
        },
        (err) => {
          console.error('Error al actualizar el cliente:', err);
          alert('Hubo un error al actualizar el cliente');
        }
      );
  }

  // Eliminar un cliente
  deleteCliente(id: number) {
    if (confirm('¿Estás seguro de que quieres eliminar este cliente?')) {
      this.clienteService.deleteCliente(id).subscribe(
        (res) => {
          console.log('Cliente eliminado:', res);
          this.getClientes(); // Refrescar la lista de clientes
        },
        (err) => {
          console.error('Error al eliminar cliente:', err);
        }
      );
    }
  }

  // Editar un cliente: Carga los datos del cliente en el formulario para editar
  editCliente(cliente: any) {
    if (!cliente) {
      console.error('No se ha recibido el cliente');
      return;
    }

    // Asignamos los valores al formulario
    this.idAActualizar = cliente.id;

    this.newCliente = {
      id: cliente.id,
      nombre: cliente.nombre,
      apellido: cliente.apellido,
      dni: cliente.dni,
      telefono: cliente.telefono,
      email: cliente.email,
      direccion: cliente.direccion,
      ruc: cliente.ruc,
      fechaRegistro: cliente.fechaRegistro,
    };

    console.log('Cliente a editar:', this.newCliente);
    this.editing = true;
  }

  // Limpiar el formulario después de una operación
  clearForm() {
    this.newCliente = {
      id: 0,
      nombre: '',
      apellido: '',
      dni: '',
      telefono: '',
      email: '',
      direccion: '',
      ruc: '',
      fechaRegistro: '',
    };
    this.idAActualizar = 0; // Reiniciamos el ID
    this.editing = false; // Desactivamos el modo de edición
  }
}
