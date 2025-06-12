package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.entity.EstadoPedido;
import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.feign.ClienteFeign;
import com.example.pedido_db.repository.DetallePedidoRepository;
import com.example.pedido_db.repository.PedidoRepository;
import com.example.pedido_db.service.PedidoService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PedidoServiceImpl implements PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteFeign clienteFeign;
    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackCliente")
    @Override
    public List<Pedido> listarPorEstado(EstadoPedido estadoPedido) {
        // Obtener los pedidos filtrados por estado
        List<Pedido> pedidos = pedidoRepository.findByEstadoPedido(estadoPedido);

        // Cargar el cliente y los detalles para cada pedido
        for (Pedido pedido : pedidos) {
            // Verificamos si el pedido tiene un clienteId
            if (pedido.getClienteId() != null) {
                try {
                    // Obtener el cliente por el clienteId usando Feign
                    Cliente cliente = clienteFeign.listById(pedido.getClienteId()).getBody();

                    if (cliente != null) {
                        pedido.setCliente(cliente);  // Asignamos el cliente al pedido
                    } else {
                        throw new RuntimeException("Cliente no encontrado con ID: " + pedido.getClienteId());
                    }
                } catch (Exception e) {
                    // Manejo de excepciones de la llamada a clienteFeign
                    throw new RuntimeException("Error al cargar cliente con ID: " + pedido.getClienteId(), e);
                }
            }

            // Cargar detalles de los pedidos (esto se puede hacer ya que la relación @OneToMany se encargará de cargar los detalles si está bien configurada)
            if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
                // Aquí deberías cargar los detalles de Pedido si no están cargados
                // Puedes hacerlo de forma explícita si usas un repositorio para `DetallePedido`
                List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
                pedido.setDetalles(detalles);  // Asignamos los detalles al pedido
            }
        }

        return pedidos;  // Devolvemos la lista de pedidos con clientes y detalles cargados
    }



    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackCliente")
    @Override
    public List<Pedido> listar() {
        // Traemos todos los pedidos desde la base de datos (esto incluye la relación con DetallePedido si la configuraste correctamente)
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Cargar el cliente y los detalles para cada pedido
        for (Pedido pedido : pedidos) {
            // Verificamos si el pedido tiene un clienteId
            if (pedido.getClienteId() != null) {
                try {
                    // Obtener el cliente por el clienteId usando Feign
                    Cliente cliente = clienteFeign.listById(pedido.getClienteId()).getBody();

                    if (cliente != null) {
                        pedido.setCliente(cliente);  // Asignamos el cliente al pedido
                    } else {
                        throw new RuntimeException("Cliente no encontrado con ID: " + pedido.getClienteId());
                    }
                } catch (Exception e) {
                    // Manejo de excepciones de la llamada a clienteFeign
                    throw new RuntimeException("Error al cargar cliente con ID: " + pedido.getClienteId(), e);
                }
            }

            // Cargar detalles de los pedidos (esto se puede hacer ya que la relación @OneToMany se encargará de cargar los detalles si está bien configurada)
            if (pedido.getDetalles() == null || pedido.getDetalles().isEmpty()) {
                // Aquí deberías cargar los detalles de Pedido si no están cargados
                // Puedes hacerlo de forma explícita si usas un repositorio para `DetallePedido`
                List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedido.getId());
                pedido.setDetalles(detalles);  // Asignamos los detalles al pedido
            }
        }

        return pedidos;  // Devolvemos la lista de pedidos con clientes y detalles cargados
    }





    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackCliente")
    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id); // Obtener solo el pedido por ID

        // Si el pedido existe, cargar el cliente y los detalles asociados
        if (pedido.isPresent()) {
            Pedido pedidoFound = pedido.get(); // Obtener el pedido encontrado

            // Verificar si el pedido tiene un clienteId y cargar el cliente
            if (pedidoFound.getClienteId() != null) {
                try {
                    // Obtener el cliente por el clienteId usando Feign
                    Cliente cliente = clienteFeign.listById(pedidoFound.getClienteId()).getBody();

                    if (cliente != null) {
                        pedidoFound.setCliente(cliente); // Asignar cliente al pedido
                    } else {
                        throw new RuntimeException("Cliente no encontrado con ID: " + pedidoFound.getClienteId());
                    }
                } catch (Exception e) {
                    // Manejo de excepciones de la llamada a clienteFeign
                    throw new RuntimeException("Error al cargar cliente con ID: " + pedidoFound.getClienteId(), e);
                }
            }

            // Cargar detalles de los pedidos (esto se puede hacer ya que la relación @OneToMany se encargará de cargar los detalles si está bien configurada)
            if (pedidoFound.getDetalles() == null || pedidoFound.getDetalles().isEmpty()) {
                // Aquí deberías cargar los detalles de Pedido si no están cargados
                // Puedes hacerlo de forma explícita si usas un repositorio para `DetallePedido`
                List<DetallePedido> detalles = detallePedidoRepository.findByPedidoId(pedidoFound.getId());
                pedidoFound.setDetalles(detalles);  // Asignamos los detalles al pedido
            }

            return Optional.of(pedidoFound); // Devolver el pedido con cliente y detalles cargados
        }

        return Optional.empty(); // Si el pedido no existe, devolver Optional vacío
    }









    @Override
    public Pedido guardar(Pedido pedido) {

        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        // Aquí puedes añadir lógica de validación si es necesario
        return pedidoRepository.save(pedido);
    }


    // Eliminar un pedido por ID
    @Override
    public void eliminar(Integer id) {
        // Eliminamos el pedido por su ID
        pedidoRepository.deleteById(id);
    }
}












