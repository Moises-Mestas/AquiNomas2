package com.example.serviciopedido.service.impl;

import com.example.serviciopedido.entity.Pedido;
import com.example.serviciopedido.feign.ServicioClienteFeignClient;
import com.example.serviciopedido.repository.PedidoRepository;
import com.example.serviciopedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ServicioClienteFeignClient administradorFeignClient;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ServicioClienteFeignClient administradorFeignClient) {
        this.pedidoRepository = pedidoRepository;
        this.administradorFeignClient = administradorFeignClient;
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        // Aquí no se hace la llamada adicional, Feign y el fallback se encargan de obtener la información
        return pedidos;
    }

    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);
        if (pedidoOpt.isPresent()) {
            // Ya no se hace una llamada a Feign adicional, se puede manejar el pedido con la data del repositorio
            return Optional.of(pedidoOpt.get());
        }

        return Optional.empty();
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        // Guardar el pedido en la base de datos
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        // Actualizar el pedido en la base de datos
        return pedidoRepository.save(pedido);
    }

    @Override
    public void eliminar(Integer id) {
        // Eliminar el pedido por ID
        pedidoRepository.deleteById(id);
    }
}
