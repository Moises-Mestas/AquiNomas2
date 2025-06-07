package com.example.serviciopedido.service.impl;

import com.example.serviciopedido.entity.Pedido;
import com.example.serviciopedido.repository.PedidoRepository;
import com.example.serviciopedido.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoServiceImpl implements PedidoService {

    private final PedidoRepository pedidoRepository;

    @Autowired
    public PedidoServiceImpl(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public List<Pedido> listar() {
        return pedidoRepository.findAll();
    }

    @Override
    public Optional<Pedido> listarPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    @Override
    public Pedido guardar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }

    @Override
    public Pedido actualizar(Pedido pedido) {
        if (pedido.getId() != null && pedidoRepository.existsById(pedido.getId())) {
            return pedidoRepository.save(pedido);
        }
        throw new RuntimeException("Pedido no encontrado");
    }

    @Override
    public void eliminar(Integer id) {
        if (pedidoRepository.existsById(id)) {
            pedidoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Pedido no encontrado");
        }
    }
}
