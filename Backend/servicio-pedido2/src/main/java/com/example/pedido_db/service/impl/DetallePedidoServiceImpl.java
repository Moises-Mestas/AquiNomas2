package com.example.pedido_db.service.impl;

import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.repository.DetallePedidoRepository;
import com.example.pedido_db.service.DetallePedidoService;
import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.feign.ClienteFeign;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private ClienteFeign clienteFeign;

    // Nombre del CircuitBreaker: 'clienteCircuitBreaker' definido en application.yml
    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackCliente")
    @Override
    public List<DetallePedido> listar() {
        List<DetallePedido> detalles = detallePedidoRepository.findAll();

        // Iterar para cargar el cliente de cada detalle de pedido
        for (DetallePedido detalle : detalles) {
            if (detalle.getClienteId() != null) {
                Cliente cliente = clienteFeign.listById(detalle.getClienteId()).getBody();
                detalle.setCliente(cliente);
            }
        }

        return detalles;
    }

    // Método fallback que se invoca si hay una falla en la llamada al servicio del Cliente
    public List<DetallePedido> fallbackCliente(Exception ex) {
        // En caso de error, puedes devolver una lista vacía o algún valor predeterminado
        // También podrías loggear el error o realizar otras acciones
        return List.of();  // Devuelve una lista vacía
    }

    @CircuitBreaker(name = "clienteCircuitBreaker", fallbackMethod = "fallbackCliente")
    @Override
    public Optional<DetallePedido> listarPorId(Integer id) {
        Optional<DetallePedido> detallePedido = detallePedidoRepository.findById(id);

        // Si el detalle de pedido existe y tiene un clienteId, cargar el cliente
        detallePedido.ifPresent(detalle -> {
            if (detalle.getClienteId() != null) {
                Cliente cliente = clienteFeign.listById(detalle.getClienteId()).getBody();
                detalle.setCliente(cliente);
            }
        });

        return detallePedido;
    }

    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public DetallePedido actualizar(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public void eliminar(Integer id) {
        detallePedidoRepository.deleteById(id);
    }
}
