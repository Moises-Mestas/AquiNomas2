package com.example.serviciopedido.service.impl;

import com.example.serviciopedido.entity.Pedido;
import com.example.serviciopedido.repository.PedidoRepository;
import com.example.serviciopedido.dto.AdministradorDTO;
import com.example.serviciopedido.feign.ServicioClienteFeignClient;
import com.example.serviciopedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ServicioClienteFeignClient servicioClienteFeignClient;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository, ServicioClienteFeignClient servicioClienteFeignClient) {
        this.pedidoRepository = pedidoRepository;
        this.servicioClienteFeignClient = servicioClienteFeignClient;
    }

    @Override
    public List<Pedido> listar() {
        List<Pedido> pedidos = pedidoRepository.findAll();

        for (Pedido pedido : pedidos) {
            AdministradorDTO administrador = servicioClienteFeignClient.getAdministradorById(pedido.getAdministrador().getId());
            pedido.setAdministrador(administrador);
        }

        return pedidos;
    }

    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(id);

        pedidoOpt.ifPresent(pedido -> {
            AdministradorDTO administrador = servicioClienteFeignClient.getAdministradorById(pedido.getAdministrador().getId());
            pedido.setAdministrador(administrador);
        });

        return pedidoOpt;
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public void eliminar(Integer id) {
        pedidoRepository.deleteById(id);
    }
}
