package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.feign.ClienteFeign;
import com.example.pedido_db.repository.PedidoRepository;
import com.example.pedido_db.service.PedidoService;
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
    private RestTemplate restTemplate;

    public List<Pedido> listar() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Cargar las relaciones cliente de manera similar a como se hace en listarPorId
        for (Pedido pedido : pedidos) {
            // Llamada a ClienteFeign para obtener el cliente (ahora de DetallePedido, no de Pedido)
            List<DetallePedido> detallePedidos = pedido.getDetalle().stream().map(detallePedido -> {
                // Llamar al ClienteFeign para obtener el cliente por clienteId
                Cliente cliente = clienteFeign.listById(detallePedido.getClienteId()).getBody();
                detallePedido.setCliente(cliente); // Asignar cliente a cada DetallePedido
                return detallePedido;
            }).collect(Collectors.toList());

            pedido.setDetalle(detallePedidos); // Asignar la lista de DetallePedido al Pedido
        }

        return pedidos;
    }

    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        try {
            Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

            // Cargar Detalles de Pedido y Cliente
            List<DetallePedido> pedidoDetalles = pedido.getDetalle().stream().map(detallePedido -> {
                // Llamar al ClienteFeign para obtener el cliente
                Cliente cliente = clienteFeign.listById(detallePedido.getClienteId()).getBody();
                detallePedido.setCliente(cliente); // Asignar cliente a DetallePedido
                return detallePedido;
            }).collect(Collectors.toList());

            pedido.setDetalle(pedidoDetalles); // Asignar la lista de DetallePedido al Pedido
            return Optional.of(pedido);
        } catch (Exception e) {
            // Log de error para detectar la causa del fallo
            System.err.println("Error al procesar el pedido con ID " + id + ": " + e.getMessage());
            e.printStackTrace();
            return Optional.empty();
        }
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