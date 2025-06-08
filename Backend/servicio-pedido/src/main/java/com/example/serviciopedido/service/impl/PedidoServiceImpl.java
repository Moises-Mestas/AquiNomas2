package com.example.serviciopedido.service.impl;

import com.example.serviciopedido.dto.Administrador;
import com.example.serviciopedido.dto.Cliente;
import com.example.serviciopedido.entity.Pedido;
import com.example.serviciopedido.feign.ServicioFeign;
import com.example.serviciopedido.repository.PedidoRepository;
import com.example.serviciopedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ServicioFeign servicioFeign;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ServicioFeign servicioFeign) {
        this.pedidoRepository = pedidoRepository;
        this.servicioFeign = servicioFeign;
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        // Para cada pedido, obtener el administrador y cliente a través del Feign combinado
        for (Pedido pedido : pedidos) {
            // Obtener el administrador asociado al pedido
            Administrador administrador = servicioFeign.getAdministradorById(pedido.getAdministrador().getId()).getBody();
            pedido.setAdministrador(administrador);

            // Obtener el cliente asociado al pedido
            Cliente cliente = servicioFeign.getClienteById(pedido.getDetallePedido().getId()).getBody();
            pedido.getDetallePedido().setCliente(cliente);
        }

        return pedidos;
    }

    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

        if (pedidoOpt.isPresent()) {
            Pedido pedido = pedidoOpt.get();
            // Obtener el administrador asociado al pedido
            Administrador administrador = servicioFeign.getAdministradorById(pedido.getAdministrador().getId()).getBody();
            pedido.setAdministrador(administrador);

            // Obtener el cliente asociado al pedido
            Cliente cliente = servicioFeign.getClienteById(pedido.getDetallePedido().getId()).getBody();
            pedido.getDetallePedido().setCliente(cliente);
        }

        return pedidoOpt;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        Optional<Pedido> pedidoExistente = pedidoRepository.findById(pedido.getId());
        if (pedidoExistente.isPresent()) {
            return pedidoRepository.save(pedido);
        } else {
            throw new RuntimeException("Pedido no encontrado con id: " + pedido.getId());
        }
    }

    @Override
    public void eliminar(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Pedido no encontrado con id: " + id);
        }
    }
}
