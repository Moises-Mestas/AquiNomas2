package com.example.servicioventa.service;



import com.example.servicioventa.entity.Promocion;

import java.util.List;
import java.util.Optional;

public interface PromocionService {
    public List<Promocion> listar();
    public Promocion guardar(Promocion promocion);
    public Promocion actualizar(Promocion promocion);
    public Optional<Promocion> listarPorId(Long id);
    public void eliminarPorId(Long id);
    public List<Promocion> buscarPorMotivo(String motivo);

}