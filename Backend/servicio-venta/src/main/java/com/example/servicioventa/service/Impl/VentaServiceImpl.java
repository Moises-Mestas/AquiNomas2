package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public List<Venta> listar() {
        return ventaRepository.findAll();
    }

    @Override
    @Transactional
    public Venta guardarVenta(Venta venta) {
        return ventaRepository.save(venta);
    }

    @Override
    @Transactional
    public Venta actualizar(Venta venta) {
        if (ventaRepository.existsById(venta.getId())) {
            return ventaRepository.save(venta);
        }
        throw new RuntimeException("Venta no encontrada");
    }

    @Override
    public Optional<Venta> listarPorId(Long id) {
        return ventaRepository.findById(id);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        if (ventaRepository.existsById(id)) {
            ventaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Venta no encontrada");
        }
    }
}