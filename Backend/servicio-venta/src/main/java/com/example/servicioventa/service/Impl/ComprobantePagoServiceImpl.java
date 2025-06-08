package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComprobantePagoServiceImpl implements ComprobantePagoService {
    @Override
    public List<ComprobantePago> listar() {
        return List.of();
    }

    @Override
    public ComprobantePago guardarComprobante(ComprobantePago comprobantePago) {
        return null;
    }

    @Override
    public ComprobantePago guardar(ComprobantePago comprobantePago) {
        return null;
    }

    @Override
    public ComprobantePago actualizar(ComprobantePago comprobantePago) {
        return null;
    }

    @Override
    public Optional<ComprobantePago> listarPorId(Integer id) {
        return Optional.empty();
    }

    @Override
    public void eliminarPorId(Integer id) {

    }
}
