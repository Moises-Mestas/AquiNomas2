package com.example.pedido_db.service.impl;

import com.example.pedido_db.dto.Cliente;
import com.example.pedido_db.entity.DetallePedido;
import com.example.pedido_db.entity.Pedido;
import com.example.pedido_db.feign.ClienteFeign;
import com.example.pedido_db.repository.DetallePedidoRepository;
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
    private DetallePedidoRepository detallePedidoRepository;


    @Override
    public List<Pedido> listar() {
        // Traemos todos los pedidos desde la base de datos
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Cargar los detalles para cada pedido
        for (Pedido pedido : pedidos) {
            // Usamos el detallePedidoId para cargar el detalle asociado
            DetallePedido detallePedido = detallePedidoRepository.findById(pedido.getDetallePedidoId())
                    .orElseThrow(() -> new RuntimeException("DetallePedido no encontrado"));

            // Verificamos si tiene clienteId y lo asignamos
            if (detallePedido.getClienteId() != null) {
                Cliente cliente = clienteFeign.listById(detallePedido.getClienteId()).getBody();
                detallePedido.setCliente(cliente); // Asignamos el cliente al detalle del pedido
            }

            // Asignamos el detallePedido al pedido
            pedido.setDetallePedido(detallePedido);
        }

        return pedidos; // Devolvemos la lista de pedidos con sus detalles y clientes cargados
    }


    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        Optional<Pedido> pedido = pedidoRepository.findById(id); // Obtener solo el pedido por ID

        // Si el pedido existe, cargar los detalles y cliente asociado
        if (pedido.isPresent()) {
            Pedido pedidoFound = pedido.get(); // Obtener el pedido encontrado

            // Usamos el detallePedidoId para obtener el detalle relacionado
            DetallePedido detallePedido = detallePedidoRepository.findById(pedidoFound.getDetallePedidoId())
                    .orElseThrow(() -> new RuntimeException("DetallePedido no encontrado"));

            // Verificar si el detallePedido tiene un clienteId y cargar el cliente
            if (detallePedido.getClienteId() != null) {
                // Llamada al ClienteFeign para obtener el cliente por clienteId
                Cliente cliente = clienteFeign.listById(detallePedido.getClienteId()).getBody();
                detallePedido.setCliente(cliente); // Asignar cliente a DetallePedido
            }

            // Asignamos el detallePedido al pedido
            pedidoFound.setDetallePedido(detallePedido);

            return Optional.of(pedidoFound); // Devolver el pedido con el detalle y cliente cargado
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