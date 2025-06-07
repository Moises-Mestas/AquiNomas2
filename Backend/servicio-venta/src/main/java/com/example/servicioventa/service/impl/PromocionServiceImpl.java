package com.example.servicioventa.service.impl;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.service.PromocionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromocionServiceImpl implements PromocionService {
    @Autowired
    private PromocionRepository promocionRepository;

    @Override
    public List<Promocion> listar() {
        return promocionRepository.findAll();
    }

    @Override
    public Promocion guardarPromocion(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    @Override
    public Promocion actualizar(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    @Override
    public Optional<Promocion> listarPorId(Integer id) {
        return promocionRepository.findById(id);
    }

    @Override
    public void eliminarPorId(Integer id) {
        promocionRepository.deleteById(id);
    }

    @Override
    public List<Promocion> buscarPorMotivo(String motivo) {
        return promocionRepository.findByMotivoContainingIgnoreCase(motivo);
    }
}
