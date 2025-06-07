package com.example.serviciopedido.service;

import com.example.serviciopedido.entity.Receta;
import java.util.List;
import java.util.Optional;

public interface RecetaService {
    List<Receta> listar();
    Optional<Receta> listarPorId(Integer id);
    Receta guardar(Receta receta);
    Receta actualizar(Receta receta);
    void eliminar(Integer id);
}
