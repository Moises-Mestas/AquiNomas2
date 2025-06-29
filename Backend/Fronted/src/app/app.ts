import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ApiService } from './core/services/api.services';
import { MenuService } from './core/services/menu.services';
import { RecetaService } from './core/services/receta.services';
import { ClienteService } from './core/services/cliente.services';
import {PedidoServices} from './core/services/pedido.services';
import {DetallePedidoService} from './core/services/detallePedido.services';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, FormsModule, CommonModule],  // Solo importar los módulos comunes
  templateUrl: './app.html',
  styleUrls: ['./app.css'],
})
export class App {
  protected title = 'Fronted';

// Declaración de las variables para Clientes
  idAEliminarCliente: number = 0;
  idAActualizarCliente: number = 0;
  newCliente: any = { nombre: '', apellido: '', dni: '', telefono: '', email: '', direccion: '', ruc: '', fechaRegistro: '' };  // Datos de cliente
  clientes: any[] = [];  // Lista de clientes
  editing: boolean = false; // Control de edición

  // Declaración de las variables para menús
  idAEliminar: number = 0;
  idAActualizar: number = 0;
  newMenu: any = { nombre: '', descripcion: '', precio: 0, tipo: '', imagen: '' };
  menus: any[] = [];  // Esta es la propiedad para almacenar los menús

  // Declaración de las variables para recetas
  recetaIdAEliminar: number = 0;
  recetaIdAActualizar: number = 0;
  newReceta: any = { cantidad: 0, descripcion: '', producto_id: 0, unidadMedida: '', menu_id: 0, cantidadDisponible: 0 };
  recetas: any[] = []; // Esta es la propiedad para almacenar las recetas

  // Declaración de las variables para detalle_pedido
  detallePedidoIdAEliminar: number = 0;
  detallePedidoIdAActualizar: number = 0;
  newDetallePedido: any = { cantidad: 0, menu_id: 0, pedido_id: 0 }; // Información del detalle pedido
  detallePedidos: any[] = [];



  pedidoIdAEliminar: number = 0;
  pedidoIdAActualizar: number = 0;
  newPedido: any = { cliente_id: 0, estado_pedido: '', fecha_pedido: '' }; // Información del pedido
  pedidos: any[] = [];

  constructor(
    private apiService: ApiService,
    private menuService: MenuService,
    private recetaService: RecetaService,
    private pedidoService: PedidoServices,
    private detallePedidoService: DetallePedidoService, // Servicio de detallePedido
    private clienteService: ClienteService

  ) {}

  // Cargar los menús y las recetas
  ngOnInit() {
    this.getMenus(); // Cargar menús
    this.getRecetas(); // Cargar recetas
    this.getPedidos(); // Cargar pedidos
    this.getDetallePedidos(); // Cargar detallePedidos
    this.getClientes();  // Llamamos al método para obtener los clientes

  }
  clearForm() {
    this.newCliente = { id: 0, nombre: '', apellido: '', dni: '', telefono: '', email: '', direccion: '', ruc: '', fechaRegistro: '' };
    this.idAActualizar = 0; // Reiniciamos el ID
    this.editing = false; // Desactivamos el modo de edición
  }
  getClientes() {
    this.clienteService.getClientes().subscribe(
      (response) => {
        this.clientes = response;  // Guardamos los clientes obtenidos
        console.log(this.clientes); // Verificamos los datos en consola
      },
      (err) => console.error('Error al obtener los clientes:', err)
    );
  }

  createCliente() {
    this.clienteService.createCliente(this.newCliente).subscribe(
      (res) => {
        console.log('Cliente creado:', res);
        this.getClientes(); // Refresca la lista de clientes
        this.newCliente = { nombre: '', apellido: '', dni: '', telefono: '', email: '', direccion: '', ruc: '', fechaRegistro: '' };  // Limpia el formulario
      },
      (err) => console.error('Error al crear el cliente:', err)
    );
  }

// Actualizar un cliente
  updateCliente() {
    this.clienteService.updateCliente(this.idAActualizarCliente, this.newCliente).subscribe(
      (res) => {
        console.log('Cliente actualizado:', res);
        this.getClientes(); // Refresca la lista de clientes
        this.clearForm(); // Limpiar el formulario
      },
      (err) => console.error('Error al actualizar el cliente:', err)
    );
  }

// Eliminar un cliente
  deleteCliente(id: number) {
    if (confirm('¿Estás seguro de que quieres eliminar este cliente?')) {
      this.clienteService.deleteCliente(id).subscribe(
        (res) => {
          console.log('Cliente eliminado:', res);
          this.getClientes(); // Refresca la lista de clientes
        },
        (err) => console.error('Error al eliminar cliente:', err)
      );
    }
  }
  // Método para obtener todos los menús
  getMenus() {
    this.menuService.getMenus().subscribe(
      (response) => {
        this.menus = response; // Asigna los menús obtenidos a la propiedad 'menus'
        console.log(this.menus);
      },
      (err) => console.error('Error al obtener los menús:', err)
    );
  }

  // Método para obtener todas las recetas
  getRecetas() {
    this.recetaService.getRecetas().subscribe(
      (response) => {
        this.recetas = response; // Asigna las recetas obtenidas a la propiedad 'recetas'
        console.log(this.recetas);
      },
      (err) => console.error('Error al obtener las recetas:', err)
    );
  }

  // Crear una receta
  crearReceta() {
    this.recetaService.createReceta(this.newReceta).subscribe(
      (res) => {
        console.log('Receta creada:', res);
        this.getRecetas();  // Refresca la lista de recetas
        this.newReceta = { cantidad: 0, descripcion: '', producto_id: 0, unidadMedida: '', menu_id: 0, cantidadDisponible: 0 };  // Limpia el formulario
      },
      (err) => console.error('Error al crear la receta:', err)
    );
  }

  // Eliminar una receta
  eliminarReceta() {
    if (!this.recetaIdAEliminar) return;

    this.recetaService.deleteReceta(this.recetaIdAEliminar).subscribe(
      (res) => {
        console.log('Receta eliminada:', res);
        this.getRecetas();  // Refresca la lista
      },
      (err) => console.error('Error al eliminar receta:', err)
    );
  }

  // Actualizar una receta
  actualizarReceta() {
    if (!this.recetaIdAActualizar) return;

    this.recetaService.updateReceta(this.recetaIdAActualizar, this.newReceta).subscribe(
      (res) => {
        console.log('Receta actualizada:', res);
        this.getRecetas();  // Refresca la lista
      },
      (err) => console.error('Error al actualizar receta:', err)
    );
  }


  getPedidos() {
    this.pedidoService.getPedidos().subscribe(
      (response) => {
        this.pedidos = response;
        console.log(this.pedidos); // Verificar que los pedidos se obtienen correctamente
      },
      (err) => console.error('Error al obtener los pedidos:', err)
    );
  }

  // Crear un pedido (Nuevo)
  crearPedido() {
    this.pedidoService.createPedido(this.newPedido).subscribe(
      (res) => {
        console.log('Pedido creado:', res);
        this.getPedidos(); // Refrescar la lista de pedidos
        this.newPedido = { clienteId: 0, estadoPedido: '', fechaPedido: '' }; // Limpiar el formulario
      },
      (err) => console.error('Error al crear el pedido:', err)
    );
  }

  // Eliminar un pedido (Nuevo)
  eliminarPedido() {
    if (!this.pedidoIdAEliminar) return;

    this.pedidoService.deletePedido(this.pedidoIdAEliminar).subscribe(
      (res) => {
        console.log('Pedido eliminado:', res);
        this.getPedidos(); // Refrescar la lista de pedidos
      },
      (err) => console.error('Error al eliminar el pedido:', err)
    );
  }


  // Método para obtener todos los detalles de pedido
  getDetallePedidos() {
    this.detallePedidoService.getDetallePedidos().subscribe(
      (response) => {
        this.detallePedidos = response;
        console.log(this.detallePedidos); // Verifica si los detalles de pedido se cargan correctamente
      },
      (err) => console.error('Error al obtener los detalles de pedido:', err)
    );
  }

  // Método para editar un detalle de pedido
  editDetallePedido(detallePedido: any) {
    // Asignamos los valores del detallePedido seleccionado a newDetallePedido
    this.detallePedidoIdAActualizar = detallePedido.id;
    this.newDetallePedido = {
      cantidad: detallePedido.cantidad,
      menu_id: detallePedido.menu.id,  // Carga el id del menú
      pedido_id: detallePedido.pedido.id // Carga el id del pedido
    };
    console.log('Datos a editar en el formulario:', this.newDetallePedido);  // Verifica los datos en consola
  }

  // Crear un detalle de pedido
  crearDetallePedido() {
    this.detallePedidoService.createDetallePedido(this.newDetallePedido).subscribe(
      (res) => {
        console.log('Detalle de pedido creado:', res);
        this.getDetallePedidos(); // Refrescar la lista de detalles de pedido
        this.newDetallePedido = { cantidad: 0, menu_id: 0, pedido_id: 0 }; // Limpiar el formulario
      },
      (err) => console.error('Error al crear el detalle de pedido:', err)
    );
  }

  // Eliminar un detalle de pedido
  eliminarDetallePedido() {
    if (!this.detallePedidoIdAEliminar) return;

    this.detallePedidoService.deleteDetallePedido(this.detallePedidoIdAEliminar).subscribe(
      (res) => {
        console.log('Detalle de pedido eliminado:', res);
        this.getDetallePedidos(); // Refrescar la lista de detalles de pedido
      },
      (err) => console.error('Error al eliminar el detalle de pedido:', err)
    );
  }

  // Actualizar un detalle de pedido
  actualizarDetallePedido() {
    if (!this.detallePedidoIdAActualizar) return;

    this.detallePedidoService.updateDetallePedido(this.detallePedidoIdAActualizar, this.newDetallePedido).subscribe(
      (res) => {
        console.log('Detalle de pedido actualizado:', res);
        this.getDetallePedidos(); // Refrescar la lista de detalles de pedido
      },
      (err) => console.error('Error al actualizar el detalle de pedido:', err)
    );
  }

}
