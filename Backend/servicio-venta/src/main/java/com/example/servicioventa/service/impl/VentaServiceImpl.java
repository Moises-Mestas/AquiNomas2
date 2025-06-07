package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {
    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    @Override
    public Venta guardarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Venta actualizar(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    public Optional<Venta> listarPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        ventaRepository.deleteById(id);
    }

    public List<Venta> buscarPorMetodoPago(String metodoPago) {
        return ventaRepository.findByMetodoPagoContainingIgnoreCase(metodoPago);
    }

    @Override
    public List<Venta> buscarPorClienteId(Integer clienteId) {
        return ventaRepository.findByPedidoId(clienteId);
    }
}
