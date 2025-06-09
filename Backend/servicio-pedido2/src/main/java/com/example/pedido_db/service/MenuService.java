package com.example.pedido_db.service;


import com.example.pedido_db.entity.Menu;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> listar();
    Optional<Menu> listarPorId(Integer id);
    Menu guardar(Menu menu);
    Menu actualizar(Menu menu);
    void eliminar(Integer id);

    // Método para listar los menús por un rango de precio
    List<Menu> listarPorRangoPrecio(BigDecimal minPrecio, BigDecimal maxPrecio);
}
