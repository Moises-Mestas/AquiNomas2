package com.example.serviciopedido.service.impl;

import com.example.serviciopedido.entity.DetallePedido;
import com.example.serviciopedido.repository.DetallePedidoRepository;
import com.example.serviciopedido.dto.ClienteDTO;
import com.example.serviciopedido.feign.ServicioClienteFeignClient;
import com.example.serviciopedido.service.DetallePedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetallePedidoServiceImpl implements DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final ServicioClienteFeignClient servicioClienteFeignClient;

    @Autowired
    public DetallePedidoServiceImpl(DetallePedidoRepository detallePedidoRepository, ServicioClienteFeignClient servicioClienteFeignClient) {
        this.detallePedidoRepository = detallePedidoRepository;
        this.servicioClienteFeignClient = servicioClienteFeignClient;
    }

    @Override
    public List<DetallePedido> listar() {
        List<DetallePedido> detalles = detallePedidoRepository.findAll();

        for (DetallePedido detalle : detalles) {
            ClienteDTO cliente = servicioClienteFeignClient.getClienteById(detalle.getCliente().getId());
            detalle.setCliente(cliente);
        }

        return detalles;
    }

    @Override
    public Optional<DetallePedido> listarPorId(Integer id) {
        Optional<DetallePedido> detalleOpt = detallePedidoRepository.findById(id);

        detalleOpt.ifPresent(detalle -> {
            ClienteDTO cliente = servicioClienteFeignClient.getClienteById(detalle.getCliente().getId());
            detalle.setCliente(cliente);
        });

        return detalleOpt;
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
