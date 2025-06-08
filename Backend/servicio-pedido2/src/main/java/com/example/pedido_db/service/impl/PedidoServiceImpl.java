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
        Optional<Pedido> pedido = pedidoRepository.findById(id); // Obtener solo el pedido por ID

        // Si el pedido existe, cargar los detalles de los pedidos asociados
        if (pedido.isPresent()) {
            Pedido pedidoFound = pedido.get(); // Obtener el pedido encontrado

            // Cargar las relaciones cliente de manera similar a como se hace en listar
            List<DetallePedido> detallePedidos = pedidoFound.getDetalle().stream().map(detallePedido -> {
                // Llamada a ClienteFeign para obtener el cliente por clienteId
                Cliente cliente = clienteFeign.listById(detallePedido.getClienteId()).getBody();
                detallePedido.setCliente(cliente); // Asignar cliente a cada DetallePedido
                return detallePedido;
            }).collect(Collectors.toList());

            pedidoFound.setDetalle(detallePedidos); // Asignar la lista de DetallePedido al Pedido

            return Optional.of(pedidoFound); // Devolver el pedido con los detalles cargados
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