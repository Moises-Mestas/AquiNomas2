package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.Venta;
import com.example.servicioventa.repository.VentaRepository;
import com.example.servicioventa.service.VentaService;
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
        ventaRepository.deleteById(Long.valueOf(id));
    }

}