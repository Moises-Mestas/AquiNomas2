package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.service.ComprobantePagoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComprobantePagoServiceImpl implements ComprobantePagoService {
    @Autowired
    private ComprobantePagoRepository comprobantePagoRepository;

    @Override
    public List<ComprobantePago> listar() {
        return comprobantePagoRepository.findAll();
    }

    @Override
    public ComprobantePago guardarComprobante(ComprobantePago comprobantePago) {
        return comprobantePagoRepository.save(comprobantePago);
    }

    @Override
    public ComprobantePago actualizar(ComprobantePago comprobantePago) {
        return comprobantePagoRepository.save(comprobantePago);
    }

    @Override
    public Optional<ComprobantePago> listarPorId(Integer id) {
        return comprobantePagoRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        comprobantePagoRepository.deleteById(id);
    }

    @Override
    public List<ComprobantePago> buscarPorNumeroSerie(String numeroSerie) {
        return comprobantePagoRepository.findByNumeroSerieContainingIgnoreCase(numeroSerie);
    }
}
