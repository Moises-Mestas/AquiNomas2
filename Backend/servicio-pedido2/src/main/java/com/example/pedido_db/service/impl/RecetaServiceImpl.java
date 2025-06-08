package com.example.pedido_db.service.impl;

import com.example.pedido_db.entity.Receta;
import com.example.pedido_db.repository.RecetaRepository;
import com.example.pedido_db.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecetaServiceImpl implements RecetaService {

    private final RecetaRepository recetaRepository;

    @Autowired
    public RecetaServiceImpl(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @Override
    public List<Receta> listar() {
        return recetaRepository.findAll();
    }

    @Override
    public Optional<Receta> listarPorId(Integer id) {
        return recetaRepository.findById(id);
    }

    @Override
    public Receta guardar(Receta receta) {
        return recetaRepository.save(receta);
    }

    @Override
    public Receta actualizar(Receta receta) {
        if (receta.getId() != null && recetaRepository.existsById(receta.getId())) {
            return recetaRepository.save(receta);
        }
        throw new RuntimeException("Receta no encontrada");
    }

    @Override
    public void eliminar(Integer id) {
        if (recetaRepository.existsById(id)) {
            recetaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Receta no encontrada");
        }
    }
}
