package com.example.pedido_db.service;

import com.example.pedido_db.entity.Receta;

import java.util.List;
import java.util.Optional;

public interface RecetaService {
    List<Receta> listar();
    Optional<Receta> listarPorId(Integer id);
    Receta guardar(Receta receta);
    Receta actualizar(Receta receta);
    void eliminar(Integer id);
    void sincronizarDesdeInventario(Integer productoId);

}
