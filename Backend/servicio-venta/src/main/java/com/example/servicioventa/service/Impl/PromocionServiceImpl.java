package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.service.PromocionService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromocionServiceImpl implements PromocionService {

    private final PromocionRepository promocionRepository;

    public PromocionServiceImpl(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    @Override
    public List<Promocion> listar() {
        return promocionRepository.findAll();
    }

    @Override
    public Promocion guardarPromocion(Promocion promocion) {
        return null;
    }

    @Override
    public Promocion guardar(Promocion promocion) {
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
        promocionRepository.deleteById(Long.valueOf(id));
    }
}