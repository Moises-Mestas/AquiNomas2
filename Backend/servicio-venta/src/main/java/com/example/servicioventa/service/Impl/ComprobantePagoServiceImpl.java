package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.ComprobantePago;
import com.example.servicioventa.repository.ComprobantePagoRepository;
import com.example.servicioventa.service.ComprobantePagoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComprobantePagoServiceImpl implements ComprobantePagoService {

    private final ComprobantePagoRepository comprobantePagoRepository;

    public ComprobantePagoServiceImpl(ComprobantePagoRepository comprobantePagoRepository) {
        this.comprobantePagoRepository = comprobantePagoRepository;
    }

    @Override
    public List<ComprobantePago> listar() {
        return comprobantePagoRepository.findAll();
    }

    @Override
    @Transactional
    public ComprobantePago guardar(ComprobantePago comprobantePago) {
        return comprobantePagoRepository.save(comprobantePago);
    }

    @Override
    @Transactional
    public ComprobantePago actualizar(ComprobantePago comprobantePago) {
        if (comprobantePagoRepository.existsById(comprobantePago.getId())) {
            return comprobantePagoRepository.save(comprobantePago);
        }
        throw new RuntimeException("Comprobante no encontrado");
    }

    @Override
    public Optional<ComprobantePago> listarPorId(Long id) {
        return comprobantePagoRepository.findById(id);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        if (comprobantePagoRepository.existsById(id)) {
            comprobantePagoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Comprobante no encontrado");
        }
    }
}