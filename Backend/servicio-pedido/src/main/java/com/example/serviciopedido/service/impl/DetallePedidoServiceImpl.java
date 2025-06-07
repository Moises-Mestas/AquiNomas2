package com.example.serviciopedido.service.impl;

import com.example.serviciopedido.entity.DetallePedido;
import com.example.serviciopedido.repository.DetallePedidoRepository;
import com.example.serviciopedido.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;

    @Autowired
    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository) {
        this.detallePedidoRepository = detallePedidoRepository;
    }

    @Override
    public List<DetallePedido> listar() {
        return detallePedidoRepository.findAll();
    }

    @Override
    public Optional<DetallePedido> listarPorId(Integer id) {
        return detallePedidoRepository.findById(id);
    }

    @Override
    public DetallePedido guardar(DetallePedido detallePedido) {
        return detallePedidoRepository.save(detallePedido);
    }

    @Override
    public DetallePedido actualizar(DetallePedido detallePedido) {
        if (detallePedido.getId() != null && detallePedidoRepository.existsById(detallePedido.getId())) {
            return detallePedidoRepository.save(detallePedido);
        }
        throw new RuntimeException("Detalle de pedido no encontrado");
    }

    @Override
    public void eliminar(Integer id) {
        if (detallePedidoRepository.existsById(id)) {
            detallePedidoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Detalle de pedido no encontrado");
        }
    }
}
