package com.example.servicioventa.service.Impl;

import com.example.servicioventa.entity.Promocion;
import com.example.servicioventa.repository.PromocionRepository;
import com.example.servicioventa.service.PromocionService;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Promocion guardar(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    @Override
    public Promocion actualizar(Promocion promocion) {
        return promocionRepository.save(promocion);
    }

    @Override
    public List<Promocion> buscarPorMotivo(String motivo) {
        return promocionRepository.findByMotivoContainingIgnoreCase(motivo);
    }

    @Override
    public Optional<Promocion> listarPorId(Long id) {
        return promocionRepository.findById(id);
    }

    @Override
    @Transactional
    public void eliminarPorId(Long id) {
        if (promocionRepository.existsById(id)) {
            promocionRepository.deleteById(id);
        } else {
            throw new RuntimeException("Promoción no encontrada");
        }
    }
}